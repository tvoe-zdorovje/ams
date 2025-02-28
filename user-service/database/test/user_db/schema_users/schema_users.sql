SET search_path TO tap, tests, public;


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
