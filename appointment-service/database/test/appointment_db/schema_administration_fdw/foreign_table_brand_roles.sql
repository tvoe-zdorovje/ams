SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_brand_roles_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'administration_fdw',
        table_name := 'brand_roles',
        columns_with_type := ARRAY[
            'brand_id INT',
            'role_id INT'
        ],
        server_name := 'administration_fdw_db',
        schema_name_option := 'administration',
        table_name_option := 'brand_roles'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_brand_roles_table_structure');
select finish(true);
