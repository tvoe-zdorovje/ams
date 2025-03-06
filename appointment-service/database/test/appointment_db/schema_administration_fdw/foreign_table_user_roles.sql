SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_user_roles_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'administration_fdw',
        table_name := 'user_roles',
        columns_with_type := ARRAY[
            'user_id INT',
            'role_id INT'
        ],
        server_name := 'administration_fdw_db',
        schema_name_option := 'administration',
        table_name_option := 'user_roles'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_user_roles_table_structure');
select finish(true);
