SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_asset_roles_belong_to_structure() RETURNS SETOF TEXT AS $$
DECLARE
    schema_name TEXT := 'administration';
    routine_name TEXT := 'asset_roles_belong_to';
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
        'VOID',
        format('routine "%s" must have "VOID" return type', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_asset_roles_belong_to_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.asset_roles_belong_to';
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    RETURN NEXT throws_ok(
           'SELECT asset_roles_belong_to(-11, ARRAY[-12, -13]::BIGINT[])',
           'P0001',
           'Some of the roles do not belong to the organization',
           format('routine "%s" must raise exception', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_asset_roles_belong_to_structure');
select has_function('test_asset_roles_belong_to_behavior');
select finish(true);
