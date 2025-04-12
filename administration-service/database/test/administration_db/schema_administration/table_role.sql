SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_role_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'role',
        columns_with_type := ARRAY[
            'id BIGINT',
            'name VARCHAR(100)',
            'description VARCHAR(255)'
        ]
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_role_table_structure');
select finish(true);
