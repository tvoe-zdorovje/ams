SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_create_brand_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'brands';
    routine_name TEXT := 'create_brand';
BEGIN
    RETURN NEXT has_function(
        schema_name,
        routine_name,
        format('routine "%s" must exist', routine_name)
    );

    RETURN NEXT is_procedure(
        schema_name,
        routine_name,
        format('routine "%s" must be a procedure', routine_name)
    );
END;
$$ LANGUAGE plpgsql;

select plan(1);
select has_function('test_create_brand_routine_structure');
select finish(true);
