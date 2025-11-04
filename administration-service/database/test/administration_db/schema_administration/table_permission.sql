SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_permission_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'permission',
        columns_with_type := ARRAY[
            'id BIGINT',
            'name VARCHAR(100)',
            'description VARCHAR(255)'
        ]
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_permission_table_standard_roles() RETURNS SETOF TEXT AS $$
DECLARE
    owner_role_id BIGINT = 1999999999613321100;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    RETURN QUERY SELECT set_has(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s', owner_role_id),
        'SELECT id FROM administration.permission',
        'The role "Owner" must have all permissions'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_permission_table_structure');
select has_function('test_permission_table_standard_roles');
select finish(true);
