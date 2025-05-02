SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_appointments_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'appointments',
        table_name := 'appointment',
        columns_with_type := ARRAY [
            'id UUID',
            'description VARCHAR(255)',
            'client_user_id BIGINT',
            'master_user_id BIGINT',
            'manager_user_id BIGINT',
            'studio_id BIGINT',
            'status appointments.appointment_status',
            'comment VARCHAR(255)'
        ],
        primary_key := 'id'
   );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_appointments_table_behavior() RETURNS SETOF TEXT AS $$
    DECLARE
        table_name NAME := 'appointment';
        table_owner TEXT := _get_rel_owner(table_name);
        appointment_id UUID := gen_random_uuid();
        description VARCHAR(255) := 'Some important description.';
        client_user_id INT := 1;
        master_user_id INT := 2;
        manager_user_id INT := 3;
        studio_id INT := 4;
        status appointments.appointment_status := 'REQUESTED';
        comment VARCHAR(255) := 'Some important comment.';
BEGIN
    EXECUTE format('SET ROLE %I', table_owner);

    PREPARE insert_appointment AS INSERT INTO appointments."appointment" VALUES ($1, $2, $3, $4, $5, $6, $7, $8);
    RETURN NEXT lives_ok(
        format(
            'EXECUTE insert_appointment(%L, %L, %L, %L, %L, %L, %L, %L)',
            appointment_id,
            description,
            client_user_id,
            master_user_id,
            manager_user_id,
            studio_id,
            status,
            comment
        ),
        format('table "%s" must be insertable', UPPER(table_name))
    );
    PREPARE select_appointment AS select id from appointments."appointment" WHERE id=$1;
    RETURN NEXT set_eq(
        format('EXECUTE select_appointment(%L)', appointment_id),
        ARRAY[appointment_id],
        format('table "%s" must be selectable', UPPER(table_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_appointments_table_structure');
select has_function('test_appointments_table_behavior');
select finish(true);
