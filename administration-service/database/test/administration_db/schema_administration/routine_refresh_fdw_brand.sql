SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_refresh_fdw_brand_routine_structure() RETURNS SETOF TEXT AS $$
    DECLARE
        schema_name NAME := 'administration';
        routine_name NAME := 'refresh_fdw_brand';
BEGIN
    RETURN NEXT has_function(
        schema_name,
        routine_name,
        format('routine "%s" must exist', UPPER(routine_name))
    );

    RETURN NEXT is_procedure(
        schema_name,
        routine_name,
        format('routine "%s" must be a procedure', UPPER(routine_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_refresh_fdw_brand_routine_structure');
select finish(true);
