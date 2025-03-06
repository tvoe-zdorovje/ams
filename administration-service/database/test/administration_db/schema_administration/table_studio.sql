SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_studio_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'studio',
        columns_with_type := ARRAY[
            'id INT'
        ],
        primary_key := 'id'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_studio_table_structure');
select finish(true);
