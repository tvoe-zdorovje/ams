SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_create_appointment_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'appointments';
    routine_name TEXT := 'create_appointment';
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
        'UUID',
        format('routine "%s" must have "UUID" return type', routine_name)
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests.test_create_appointment_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'appointments.create_appointment';

    description VARCHAR(255) := 'my awesome description';
    client_user_id BIGINT := 123;
    master_user_id BIGINT := 456;
    manager_user_id BIGINT := 789;
    studio_id BIGINT := 0;
    comment VARCHAR(255) := 'my awesome comment';

    generated_id UUID;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('appointments'));

    SELECT appointments.create_appointment(
        description,
        client_user_id,
        master_user_id,
        manager_user_id,
        studio_id,
        comment
   ) INTO generated_id;

    RETURN NEXT matches(
        CAST(generated_id AS TEXT),
        '^[[:xdigit:]]{8}(?:\-[[:xdigit:]]{4}){3}\-[[:xdigit:]]{12}$',
        format('Routine "%s" must create a new appointment with a correctly generated ID', routine_name)
    );

    PREPARE select_appntmnt AS
        SELECT description || client_user_id || master_user_id || manager_user_id || studio_id || status || comment
        FROM appointments.appointment
        WHERE id = $1;

    RETURN NEXT results_eq(
        format('EXECUTE select_appntmnt(%L)', generated_id),
        ARRAY[description || client_user_id || master_user_id || manager_user_id || studio_id || 'REQUESTED' || comment],
        format('Routine "%s" must create an appointment with correct parameter values', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_create_appointment_routine_structure');
select has_function('test_create_appointment_routine_behavior');
select finish(true);
