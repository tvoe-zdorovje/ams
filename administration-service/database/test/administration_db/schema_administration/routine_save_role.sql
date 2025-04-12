SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_save_role_routine_structure() RETURNS SETOF TEXT AS $$
DECLARE
    schema_name TEXT := 'administration';
    routine_name TEXT := 'save_role';
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


CREATE OR REPLACE FUNCTION tests.test_save_role_routine_behavior_create_role() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.save_role';

    role_name VARCHAR(50) := 'test_role';
    role_description VARCHAR(50) := 'test role';
    permissions BIGINT[] := ARRAY[]::BIGINT[];

    actual_generated_id BIGINT;
    expected_generated_id BIGINT;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    SELECT nextval('administration.role_id_seq') + 1000000000 INTO expected_generated_id;
    SELECT administration.save_role(null, role_name, role_description, permissions) INTO actual_generated_id;

    RETURN NEXT is(
        actual_generated_id,
        expected_generated_id,
        format('Routine "%s" must create a new role with a correctly generated ID', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', actual_generated_id),
        permissions,
        format('Routine "%s" must create a new role without any permissions', routine_name)
    );


    permissions = ARRAY[-1234567891, -1234567890]::BIGINT[];
    INSERT INTO administration.permission(id, name, description)
    SELECT DISTINCT permission_id, 'test_perm_name', 'test_perm_desc'
    FROM unnest(permissions) AS permission_id;

    SELECT administration.save_role(null, role_name, role_description, permissions) INTO actual_generated_id;

    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', actual_generated_id),
        permissions,
        format('Routine "%s" must create a new role with provided permissions', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_save_role_routine_behavior_update_role() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.save_role';

    role_id BIGINT;
    initial_role_name VARCHAR(50) := 'test_role';
    updated_role_name VARCHAR(50) := 'test_role updated';
    initial_role_description VARCHAR(50) := 'test role';
    updated_role_description VARCHAR(50) := 'test role updated';
    permissions BIGINT[] := ARRAY[]::BIGINT[];
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    SELECT administration.save_role(null, initial_role_name, initial_role_description, permissions) INTO role_id;

    permissions = ARRAY[-1234567891, -1234567890]::BIGINT[];
    INSERT INTO administration.permission(id, name, description)
    SELECT DISTINCT permission_id, 'test_perm_name', 'test_perm_desc'
    FROM unnest(permissions) AS permission_id;

    SELECT administration.save_role(role_id, updated_role_name, updated_role_description, permissions) INTO role_id;

    RETURN NEXT results_eq(
        format('SELECT name || description FROM administration.role WHERE id = %s;', role_id),
        ARRAY[updated_role_name || updated_role_description],
        format('Routine "%s" must update role name and description', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', role_id),
        permissions,
        format('Routine "%s" must update role permissions', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(3);
select has_function('test_save_role_routine_structure');
select has_function('test_save_role_routine_behavior_create_role');
select has_function('test_save_role_routine_behavior_update_role');
select finish(true);
