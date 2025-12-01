SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_create_standard_role_owner_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'administration';
    routine_name TEXT := 'create_standard_role_owner';
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

CREATE OR REPLACE FUNCTION tests.test_create_standard_role_owner_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.create_standard_role_owner';

    _role_id BIGINT;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    SELECT administration.create_standard_role_owner() INTO _role_id;

    RETURN QUERY SELECT set_has(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s', _role_id),
        'SELECT id FROM administration.permission',
        format('The routine %s must create a new role "Owner" with all permissions', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_create_standard_role_owner_routine_structure');
select has_function('test_create_standard_role_owner_routine_behavior');
select finish(true);
