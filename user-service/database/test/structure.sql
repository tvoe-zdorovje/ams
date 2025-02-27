CREATE SCHEMA IF NOT EXISTS tap;
CREATE SCHEMA IF NOT EXISTS tests;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, tests, users, public;

CREATE OR REPLACE FUNCTION tests.test_schemas_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT schemas_are(ARRAY ['public', 'users', 'tap', 'tests']);
END;
$$ LANGUAGE plpgsql;

select plan(1);
select has_function('test_schemas_structure');
select finish(true);
