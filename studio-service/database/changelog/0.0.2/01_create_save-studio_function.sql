CREATE SEQUENCE IF NOT EXISTS studios.studio_id_seq START WITH 1000000000171342100 INCREMENT 1000000000;


CREATE OR REPLACE FUNCTION studios.save_studio(
    i_id BIGINT,
    i_name VARCHAR(50),
    i_description VARCHAR(50)
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'studios.studio_id_seq';
    o_id BIGINT := i_id;
BEGIN
    IF i_id IS NULL THEN
        INSERT INTO studios."studio"(
            id,
            name,
            description
        ) VALUES (
            nextval(id_seq_name),
            i_name,
            i_description
        )
        RETURNING id INTO o_id;
    ELSE
        UPDATE studios."studio"
            SET
                name = i_name,
                description = i_description
            WHERE id = i_id;
    END IF;

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
