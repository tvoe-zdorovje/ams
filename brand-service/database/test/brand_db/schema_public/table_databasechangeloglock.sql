SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_databasechangeloglock_table() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT has_table(
        'databasechangeloglock',
        'table "DATABASECHANGELOGLOCK" must exist'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_databasechangeloglock_table');
select finish(true);
