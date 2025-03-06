SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_change_owner_routine_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN NEXT has_function(
        'public',
        'change_owner',
        'routine "CHANGE_OWNER" must exist'
    );

    RETURN NEXT is_normal_function(
        'public',
        'change_owner',
        'routine "CHANGE_OWNER" must be a normal function'
    );

    RETURN NEXT function_returns(
        'public',
        'change_owner',
        'event_trigger',
        'routine "CHANGE_OWNER" must have "EVENT_TRIGGER" return type'
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests.test_change_owner_routine_behavior() RETURNS SETOF TEXT AS $$
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
        'routine "CHANGE_OWNER" must change a table owner to a schema owner'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_change_owner_routine_structure');
select has_function('test_change_owner_routine_behavior');
select finish(true);
