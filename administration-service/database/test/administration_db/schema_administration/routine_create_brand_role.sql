SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_create_brand_role_routine_structure() RETURNS SETOF TEXT AS $$
DECLARE
    schema_name TEXT := 'administration';
    routine_name TEXT := 'create_brand_role';
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


CREATE OR REPLACE FUNCTION tests.test_create_brand_role_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.create_brand_role';

    brand_id BIGINT := -1234567890;

    role_name VARCHAR(50) := 'test_role';
    role_description VARCHAR(50) := 'test role';
    permissions BIGINT[] := ARRAY [-12345, 12344]::BIGINT[];

    actual_generated_id BIGINT;
    expected_generated_id BIGINT;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    DROP TRIGGER refresh_brand_cache_trg ON administration.brand_roles;

    INSERT INTO administration.brand(id) VALUES (brand_id);

    INSERT INTO administration.permission(id, name, description)
    SELECT DISTINCT permission_id, 'test_perm_name', 'test_perm_desc'
    FROM unnest(permissions) AS permission_id;


    SELECT nextval('administration.role_id_seq') + 1000000000 INTO expected_generated_id;
    SELECT administration.create_brand_role(
        brand_id,
        role_name,
        role_description,
        permissions
   ) INTO actual_generated_id;

    RETURN NEXT is(
        actual_generated_id,
        expected_generated_id,
        format('Routine "%s" must create a new role with a correctly generated ID', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT permission_id FROM administration.role_permissions WHERE role_id = %s;', actual_generated_id),
        permissions,
        format('Routine "%s" must create a new role with provided permissions', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_create_brand_role_routine_structure');
select has_function('test_create_brand_role_routine_behavior');
select finish(true);
