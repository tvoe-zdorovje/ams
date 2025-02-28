SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_change_table_owner_trigger() RETURNS SETOF TEXT AS $$
DECLARE
    schema_owner TEXT;
BEGIN
    SELECT _get_schema_owner('public') INTO schema_owner;
    assert schema_owner != current_user;

    CREATE TABLE public.test_table(id INT);

    RETURN NEXT table_owner_is(
        'public',
        'test_table',
        schema_owner,
        'trigger "CHANGE_TABLE_OWNER" must change a table owner to a schema owner'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_change_table_owner_trigger');
select finish(true);
