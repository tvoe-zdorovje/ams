SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_role_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'administration_fdw',
        table_name := 'role',
        columns_with_type := ARRAY[
            'id INT',
            'name VARCHAR(100)',
            'description VARCHAR(255)'
        ],
        server_name := 'administration_fdw_db',
        schema_name_option := 'administration',
        table_name_option := 'role'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_role_table_structure');
select finish(true);
