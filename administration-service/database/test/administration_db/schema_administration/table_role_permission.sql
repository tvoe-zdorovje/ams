SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_role_permissions_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'role_permissions',
        columns_with_type := ARRAY[
            'role_id INT',
            'permission_id INT'
        ],
        foreign_keys := ARRAY[
            'role_id',
            'permission_id'
        ],
        unique_columns := ARRAY[
            'role_id',
            'permission_id'
        ]
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_role_permissions_table_structure');
select finish(true);
