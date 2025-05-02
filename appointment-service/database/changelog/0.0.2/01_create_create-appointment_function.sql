CREATE OR REPLACE FUNCTION appointments.create_appointment(
    i_description VARCHAR(255),
    i_client_user_id BIGINT,
    i_master_user_id BIGINT,
    i_manager_user_id BIGINT,
    i_studio_id BIGINT,
    i_comment VARCHAR(255)
) RETURNS UUID AS $$
DECLARE
    o_id UUID;
BEGIN
    INSERT INTO appointments.appointment(
        id,
        description,
        client_user_id,
        master_user_id,
        manager_user_id,
        studio_id,
        status,
        comment
    ) VALUES (
        gen_random_uuid(),
        i_description,
        i_client_user_id,
        i_master_user_id,
        i_manager_user_id,
        i_studio_id,
        'REQUESTED',
        i_comment
    )
    RETURNING id INTO o_id;

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
