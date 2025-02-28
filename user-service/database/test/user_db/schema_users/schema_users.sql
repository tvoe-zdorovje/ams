CREATE SCHEMA IF NOT EXISTS tap;
CREATE SCHEMA IF NOT EXISTS tests;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, tests, users, public;


CREATE OR REPLACE FUNCTION tests.test_users_schema_exists() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT has_schema(
        'users',
        'schema "USERS" must exist'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_users_schema_exists');
select finish(true);
