SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.foreign_table_structure(
    schema_name NAME,
    table_name NAME,
    columns_with_type TEXT[],
    server_name NAME,
    schema_name_option TEXT,
    table_name_option TEXT
) RETURNS SETOF TEXT AS $$
DECLARE
    column_with_type TEXT;
    column_names NAME[];
    column_name NAME;
    column_type TEXT;
    text_array TEXT[];
    foreign_table RECORD := _get_foreign_table(server_name, schema_name_option, table_name_option);
BEGIN
    RETURN QUERY SELECT has_foreign_table(
        schema_name,
        table_name,
        format('table "%s" must exist', UPPER(table_name))
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


    RETURN QUERY SELECT ok(
        foreign_table IS NOT NULL,
        format('foreign table "%s" must exist', UPPER(table_name))
    );
    RETURN QUERY SELECT is(
        foreign_table.server_name,
        server_name,
        format('foreign table "%s" must have "%s" server', UPPER(table_name), server_name)
    );
    RETURN QUERY SELECT is(
        foreign_table.schema_name,
        schema_name_option,
        format('foreign table "%s" must have "%s"="%s" option', UPPER(table_name), 'schema_name', schema_name_option)
    );
    RETURN QUERY SELECT is(
        foreign_table.table_name,
        table_name_option,
        format('foreign table "%s" must have "%s"="%s" option', UPPER(table_name), 'table_name', table_name_option)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests._get_foreign_table(
    _server_name NAME,
    _schema_name TEXT,
    _table_name TEXT
) RETURNS TABLE(
    server_name NAME,
    schema_name TEXT,
    table_name TEXT
) AS $$
BEGIN
    RETURN QUERY SELECT
        foreign_table.server_name,
        MAX(CASE WHEN foreign_table.key LIKE 'schema_name' THEN value END) AS schema_name,
        MAX(CASE WHEN foreign_table.key LIKE 'table_name' THEN value END) AS table_name
    FROM (
        SELECT
            pfs.srvname AS server_name,
            split_part(pft.options, '=', 1) AS key,
            split_part(pft.options, '=', 2) AS value
        FROM pg_foreign_server pfs
        LEFT JOIN (
            SELECT ftserver, UNNEST(ftoptions) AS options FROM pg_foreign_table
        ) AS pft
        ON pft.ftserver = pfs.oid
    ) AS foreign_table
    WHERE
        foreign_table.server_name = _server_name AND (
            (foreign_table.key = 'schema_name' AND foreign_table.value = _schema_name) OR
            (foreign_table.key = 'table_name' AND foreign_table.value = _table_name)
        )
    GROUP BY foreign_table.server_name;
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('tests'::name, 'foreign_table_structure'::name);
select has_function('tests'::name, '_get_foreign_table'::name);
select finish(true);
