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

    user_id BIGINT := -1234567890;

    initial_first_name VARCHAR(50) := 'initial_first_name';
    initial_last_name VARCHAR(50) := 'initial_last_name';
    initial_phone_number VARCHAR(50) := '+375297671245';

    updated_first_name VARCHAR(50) := 'updated_first_name';
    updated_last_name VARCHAR(50) := 'updated_last_name';
    updated_phone_number VARCHAR(50) := '+375291484148';

    isUserUpdated BOOLEAN;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('users'));

    INSERT INTO users."user"(id, first_name, last_name, phone_number) VALUES
        (user_id, initial_first_name, initial_last_name, initial_phone_number);


    PERFORM users.save_user(user_id, updated_first_name, updated_last_name, updated_phone_number);


    SELECT count(*)::INT::BOOLEAN INTO isUserUpdated FROM users."user" WHERE
        id = user_id AND
        first_name = updated_first_name AND
        last_name = updated_last_name AND
        phone_number = updated_phone_number;

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
