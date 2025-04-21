SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_set_brand_roles_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'administration';
    routine_name TEXT := 'set_brand_roles';
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

CREATE OR REPLACE FUNCTION tests.test_set_brand_roles_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.set_brand_roles';

    brand_id BIGINT := -1234567890;

    returned_brand_id BIGINT;
    roles BIGINT[];
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));
    DROP TRIGGER refresh_brand_cache_trg ON administration.brand_roles;

    INSERT INTO administration.brand(id) VALUES (brand_id);

    roles = ARRAY[-1234567891, -1234567890]::BIGINT[];
    INSERT INTO administration.role(id, name, description)
    SELECT DISTINCT role_id, 'test_role_name', 'test_role_desc'
    FROM unnest(roles) AS role_id;


    SELECT administration.set_brand_roles(brand_id, roles) INTO returned_brand_id;

    RETURN NEXT is(
        returned_brand_id,
        brand_id,
        format('Routine "%s" must return provided brand ID', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT role_id FROM administration.brand_roles WHERE brand_id = %s;', brand_id),
        roles,
        format('Routine "%s" must add provided roles', routine_name)
    );


    roles = ARRAY[-1234567891]::BIGINT[];

    PERFORM administration.set_brand_roles(brand_id, roles);

    RETURN NEXT results_eq(
        format('SELECT role_id FROM administration.brand_roles WHERE brand_id = %s;', brand_id),
        roles,
        format('Routine "%s" must clean roles up before saving provided ones', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_set_brand_roles_routine_structure');
select has_function('test_set_brand_roles_routine_behavior');
select finish(true);
