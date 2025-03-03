SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_appointments_schema_exists() RETURNS SETOF TEXT AS $$
    DECLARE
        schema_name NAME = 'appointments';
BEGIN
    RETURN NEXT has_schema(
        schema_name,
        format('schema "%s" must exist', UPPER(schema_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_appointments_schema_exists');
select finish(true);
