SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_databasechangelog_table() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT has_table(
        'databasechangelog',
        'table "DATABASECHANGELOG" must exist'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_databasechangelog_table');
select finish(true);
