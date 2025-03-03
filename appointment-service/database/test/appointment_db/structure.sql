SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_schemas_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT schemas_are(ARRAY ['public', 'appointments', 'administration_fdw', 'tap', 'tests']);
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_schemas_structure');
select finish(true);
