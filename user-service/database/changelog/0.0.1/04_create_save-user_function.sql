CREATE SEQUENCE IF NOT EXISTS users.user_id_seq START WITH 1000000001413121100 INCREMENT 1000000000;


CREATE OR REPLACE FUNCTION users.save_user(
    i_id BIGINT,
    i_first_name VARCHAR(50),
    i_last_name VARCHAR(50),
    i_phone_number VARCHAR(15)
) RETURNS BIGINT AS $$
BEGIN
    UPDATE users."user"
    SET
        first_name = i_first_name,
        last_name = i_last_name,
        phone_number = i_phone_number
    WHERE id = i_id;

    RETURN i_id;
END;
$$ LANGUAGE plpgsql;
