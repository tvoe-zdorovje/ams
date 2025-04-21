SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_set_role_permissions_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'administration';
    routine_name TEXT := 'set_role_permissions';
BEGIN
    RETURN NEXT has_function(
        schema_name,
        routine_name,
        format('routine "%s" must exist', routine_name)
    );

    RETURN NEXT is_normal_function(
        schema_name,
        routine_name,
        format('routine "%s" must be a normal function', routine_name)
    );

    RETURN NEXT function_returns(
        schema_name,
        routine_name,
        'BIGINT',
        format('routine "%s" must have "BIGINT" return type', routine_name)
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests.test_set_role_permissions_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.set_role_permissions';

    role_id BIGINT := -1234567890;

    returned_role_id BIGINT;
    permissions BIGINT[];
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    INSERT INTO administration.role(id, name, description) VALUES (role_id, 'test_role', 'test role');

    permissions = ARRAY[-1234567891, -1234567890]::BIGINT[];
    INSERT INTO administration.permission(id, name, description)
    SELECT DISTINCT permission_id, 'test_perm_name', 'test_perm_desc'
    FROM unnest(permissions) AS permission_id;


    SELECT administration.set_role_permissions(role_id, permissions) INTO returned_role_id;

    RETURN NEXT is(
        returned_role_id,
        role_id,
        format('Routine "%s" must return role_id', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', role_id),
        permissions,
        format('Routine "%s" must add provided permissions', routine_name)
    );


    permissions = ARRAY[-1234567891]::BIGINT[];

    PERFORM administration.set_role_permissions(role_id, permissions);

    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', role_id),
        permissions,
        format('Routine "%s" must clean permissions up before saving provided ones', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_set_role_permissions_routine_structure');
select has_function('test_set_role_permissions_routine_behavior');
select finish(true);
