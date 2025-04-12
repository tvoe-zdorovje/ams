SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_studio_roles_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'studio_roles',
        columns_with_type := ARRAY[
            'studio_id BIGINT',
            'role_id BIGINT'
        ],
        foreign_keys := ARRAY[
            'studio_id',
            'role_id'
        ],
        unique_columns := ARRAY[
            'studio_id',
            'role_id'
        ]
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_studio_roles_table_structure');
select finish(true);
