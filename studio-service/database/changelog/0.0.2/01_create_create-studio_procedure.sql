CREATE SEQUENCE IF NOT EXISTS studios.studio_id_seq START WITH 1000000001713421100 INCREMENT 1000000000;


CREATE OR REPLACE PROCEDURE studios.create_studio(
    i_name VARCHAR(50),
    i_description VARCHAR(50),
    i_owner_user_id BIGINT,

    OUT o_id BIGINT
) AS $$
DECLARE
    id_seq_name REGCLASS := 'studios.studio_id_seq';
BEGIN
    INSERT INTO studios.studio(
        id,
        name,
        description
    ) VALUES (
        nextval(id_seq_name),
        i_name,
        i_description
    )
    RETURNING id INTO o_id;
END;
$$ LANGUAGE plpgsql;
