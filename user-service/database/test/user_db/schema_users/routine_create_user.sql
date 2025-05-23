SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_create_user_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'users';
    routine_name TEXT := 'create_user';
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

CREATE OR REPLACE FUNCTION tests.test_create_user_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'users.create_user';

    actual_generated_id BIGINT;
    expected_generated_id BIGINT;

    password TEXT := 'strong_password';
    first_name VARCHAR(50) := 'first_name';
    last_name VARCHAR(50) := 'last_name';
    phone_number VARCHAR(50) := '+375297671245';
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('users'));

    SELECT nextval('users.user_id_seq') + 1000000000 INTO expected_generated_id;
    SELECT users.create_user(password, first_name, last_name, phone_number) INTO actual_generated_id;


    RETURN NEXT is(
        actual_generated_id,
        expected_generated_id,
        format('Routine "%s" must create a new user with a correctly generated ID', routine_name)
    );

    RETURN NEXT set_eq(
        format('SELECT password FROM user_password WHERE user_id = %L', actual_generated_id),
        ARRAY[password],
        format('Routine "%s" must save user password', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_create_user_routine_structure');
select has_function('test_create_user_routine_behavior');
select finish(true);
