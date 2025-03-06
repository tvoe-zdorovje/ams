SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.table_structure(
    schema_name NAME,
    table_name NAME,
    columns_with_type TEXT[]
) RETURNS SETOF TEXT AS $$
DECLARE
    column_with_type TEXT;
    column_names NAME[];
    column_name NAME;
    column_type TEXT;
    text_array TEXT[];
    schema_owner TEXT := _get_schema_owner(schema_name);
BEGIN
    RETURN QUERY SELECT has_table(
        schema_name,
        table_name,
        format('table "%s" must exist', UPPER(table_name))
    );


    RETURN QUERY SELECT table_owner_is(
        schema_name,
        table_name,
        schema_owner,
        format('table "%s" must be owned by the schema owner: "%s"', UPPER(table_name), UPPER(schema_owner))
    );


    FOREACH column_with_type IN ARRAY columns_with_type LOOP
        text_array := string_to_array(column_with_type, ' ');
        column_name := text_array[1];
        column_names := array_append(column_names, column_name);
        column_type := text_array[2];

        RETURN QUERY SELECT col_type_is(
            schema_name,
            table_name,
            column_name,
            column_type,
            format('table "%s" must have "%s" column with "%s" type', UPPER(table_name), UPPER(column_name), UPPER(column_type))
        );
    END LOOP;

    RETURN QUERY SELECT columns_are(
        schema_name,
        table_name,
        column_names,
        format('table "%s" must not have extra columns', UPPER(table_name))
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.table_structure(
    schema_name NAME,
    table_name NAME,
    columns_with_type TEXT[],
    primary_key NAME
) RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := schema_name,
        table_name := table_name,
        columns_with_type := columns_with_type
    );

    RETURN QUERY SELECT col_is_pk(
        schema_name,
        table_name,
        primary_key,
        format('table "%s" must have "%s" primary key', UPPER(table_name), UPPER(primary_key))
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.table_structure(
    schema_name NAME,
    table_name NAME,
    columns_with_type TEXT[],
    foreign_keys NAME[],
    unique_columns NAME[]
) RETURNS SETOF TEXT AS $$
    DECLARE
        foreign_key NAME;
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := schema_name,
        table_name := table_name,
        columns_with_type := columns_with_type
    );


    FOREACH foreign_key IN ARRAY foreign_keys LOOP
        RETURN QUERY SELECT col_is_fk(
            schema_name,
            table_name,
            foreign_key,
            format('table "%s" must have foreign key on the "%s" column', UPPER(table_name), UPPER(foreign_key))
        );
    END LOOP;


    IF unique_columns IS NULL OR array_length(unique_columns, 1) IS NULL THEN
        RETURN;
    END IF;

    RETURN QUERY SELECT col_is_unique(
        schema_name,
        table_name,
        unique_columns,
        format(
            'table "%s" must have an unique constraint on the following columns: %s',
            UPPER(table_name),
            unique_columns
        )
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('tests'::name, 'table_structure'::name);
select finish(true);
