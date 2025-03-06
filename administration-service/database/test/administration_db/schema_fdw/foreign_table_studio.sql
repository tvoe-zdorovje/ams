SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_studio_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'fdw',
        table_name := 'studio',
        columns_with_type := ARRAY[
            'id INT'
        ],
        server_name := 'studio_fdw_db',
        schema_name_option := 'studios',
        table_name_option := 'studio'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_studio_table_structure');
select finish(true);
