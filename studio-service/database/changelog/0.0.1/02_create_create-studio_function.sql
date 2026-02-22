CREATE SEQUENCE IF NOT EXISTS studios.studio_id_seq START WITH 1000000001713421100 INCREMENT 1000000000;


CREATE OR REPLACE FUNCTION studios.create_studio(
    i_name VARCHAR(50),
    i_description VARCHAR(50)
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'studios.studio_id_seq';
    o_id BIGINT;
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

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
