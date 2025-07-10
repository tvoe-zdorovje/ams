SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_delete_user_roles_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'administration';
    routine_name TEXT := 'delete_user_roles';
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

CREATE OR REPLACE FUNCTION tests.test_delete_user_roles_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.delete_user_roles';

    _user_id BIGINT := -1234567890;
    organization_id BIGINT := -1234567899;
    roles BIGINT[] = ARRAY[-1234567891, -1234567890]::BIGINT[];

    returned_user_id BIGINT;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    DROP TRIGGER refresh_user_cache_trg ON administration.user_roles;

    INSERT INTO administration."user"(id) VALUES (_user_id);

    INSERT INTO administration.role(id, name, description)
    SELECT DISTINCT role_id, 'test_role_name', 'test_role_desc'
    FROM unnest(roles) AS role_id;

    INSERT INTO administration.user_roles(user_id, role_id)
    SELECT DISTINCT _user_id, role_id
    FROM unnest(roles) AS role_id;

    DROP TRIGGER IF EXISTS refresh_brand_cache_trg ON administration.brand_roles;
    INSERT INTO administration.brand(id) VALUES (organization_id);

    INSERT INTO administration.brand_roles(brand_id, role_id)
    SELECT DISTINCT organization_id, role_id
    FROM unnest(roles) AS role_id;

    SELECT administration.delete_user_roles(_user_id, organization_id, roles) INTO returned_user_id;

    RETURN NEXT is(
        returned_user_id,
        _user_id,
        format('Routine "%s" must return provided brand ID', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT role_id FROM administration.user_roles WHERE user_id = %s;', _user_id),
        ARRAY[]::BIGINT[],
        format('Routine "%s" must add provided roles', routine_name)
    );

    RETURN NEXT throws_ok(
        format('SELECT administration.delete_user_roles(%L, %L, %L)', _user_id, -11, roles),
        'P0001',
        'Some of the roles do not belong to the organization',
        format('Routine "%s" must raise exception', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_delete_user_roles_routine_structure');
select has_function('test_delete_user_roles_routine_behavior');
select finish(true);
