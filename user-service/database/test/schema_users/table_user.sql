CREATE SCHEMA IF NOT EXISTS tap;
CREATE SCHEMA IF NOT EXISTS tests;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, tests, users, public;

CREATE OR REPLACE FUNCTION tests.test_users_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT has_table('users', 'user', 'table USER must exist');

    -- todo: columns, not nullable, PK, FK...
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_users_table_structure');
select finish(true);
