SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_next_id_routine_structure() RETURNS SETOF TEXT AS $$
DECLARE
    schema_name NAME := 'users';
    routine_name NAME := 'next_id';
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

CREATE OR REPLACE FUNCTION tests.test_next_id_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    seq_name REGCLASS := 'users.user_id_seq';
    actual BIGINT;
    expected BIGINT;
BEGIN
    expected := format('%s3121100', nextval(seq_name) + 100);

    actual := users.next_id(seq_name);

    RETURN NEXT is(
        actual,
        expected,
        'Routine users.next_id must return correct result'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_next_id_routine_structure');
select has_function('test_next_id_routine_behavior');
select finish(true);
