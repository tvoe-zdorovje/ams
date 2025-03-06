SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_user_roles_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'user_roles',
        columns_with_type := ARRAY[
            'user_id INT',
            'role_id INT'
        ],
        foreign_keys := ARRAY[
            'role_id',
            'user_id'
        ],
        unique_columns := ARRAY[
            'user_id',
            'role_id'
        ]
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_user_roles_table_structure');
select finish(true);
