CREATE SEQUENCE IF NOT EXISTS administration.role_id_seq START WITH 1000000001613321100 INCREMENT 1000000000;


CREATE OR REPLACE FUNCTION administration.save_role(
    i_id BIGINT,
    i_name VARCHAR(50),
    i_description VARCHAR(50),
    i_permissions BIGINT[]
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'administration.role_id_seq';

    o_id BIGINT := i_id;
BEGIN
    IF i_id IS NULL THEN
        INSERT INTO administration."role"(
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
        UPDATE administration."role"
            SET
                name = i_name,
                description = i_description
            WHERE id = i_id;
    END IF;


    IF i_permissions IS NULL OR array_length(i_permissions, 1) IS NULL THEN
        RETURN o_id;
    END IF;

    PERFORM administration.save_role_permissions(o_id, i_permissions);

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
