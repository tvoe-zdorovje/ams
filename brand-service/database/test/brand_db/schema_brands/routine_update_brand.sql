SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_update_brand_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'brands';
    routine_name TEXT := 'update_brand';
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


select plan(1);
select has_function('test_update_brand_routine_structure');
select finish(true);
