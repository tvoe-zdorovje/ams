CREATE SEQUENCE IF NOT EXISTS users.user_id_seq START WITH 100000000141 INCREMENT 100;


CREATE OR REPLACE FUNCTION users.next_id(
    seq_name REGCLASS
) RETURNS BIGINT AS $$
DECLARE
    postfix TEXT := '3121100';
BEGIN
    RETURN CAST((nextval(seq_name) || postfix) AS BIGINT);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION users.save_user(
    i_id BIGINT,
    i_first_name VARCHAR(50),
    i_last_name VARCHAR(50)
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'users.user_id_seq';
    o_id BIGINT := i_id;
BEGIN
    IF i_id IS NULL THEN
        INSERT INTO users."user"(
            id,
            first_name,
            last_name
        ) VALUES (
            users.next_id(id_seq_name),
            i_first_name,
            i_last_name
        )
        RETURNING id INTO o_id;
    ELSE
        UPDATE users."user"
            SET
                first_name = i_first_name,
                last_name = i_last_name
            WHERE id = i_id;
    END IF;

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
