SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_save_user_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'users';
    routine_name TEXT := 'save_user';
BEGIN
    RETURN NEXT has_function(
        schema_name,
        routine_name,
        format('routine "%s" must exist', routine_name)
    );

    RETURN NEXT is_normal_function(
        schema_name,
        routine_name,
        format('routine "%s" must be a normal function', routine_name)
    );

    RETURN NEXT function_returns(
        schema_name,
        routine_name,
        'BIGINT',
        format('routine "%s" must have "BIGINT" return type', routine_name)
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests.test_save_user_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'users.save_user';

    actual_generated_id BIGINT;
    expected_generated_id BIGINT;

    initial_first_name VARCHAR(50) := 'initial_first_name';
    initial_last_name VARCHAR(50) := 'initial_last_name';

    updated_first_name VARCHAR(50) := 'updated_first_name';
    updated_last_name VARCHAR(50) := 'updated_last_name';

    isUserUpdated BOOLEAN;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('users'));

    SELECT users.save_user(null, initial_first_name, initial_last_name) INTO actual_generated_id;
    SELECT users.next_id('users.user_id_seq') - 1000000000 INTO expected_generated_id;


    RETURN NEXT is(
        actual_generated_id,
        expected_generated_id,
        format('Routine "%s" must create a new user with a correctly generated ID', routine_name)
    );


    PERFORM users.save_user(actual_generated_id, updated_first_name, updated_last_name);


    SELECT count(*)::INT::BOOLEAN INTO isUserUpdated FROM users."user" WHERE
        id = actual_generated_id AND
        first_name = updated_first_name AND
        last_name = updated_last_name;

    RETURN NEXT ok(
        isUserUpdated,
        format('Routine "%s" must update the user correctly', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_save_user_routine_structure');
select has_function('test_save_user_routine_behavior');
select finish(true);
