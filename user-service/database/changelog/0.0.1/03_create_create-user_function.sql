CREATE OR REPLACE FUNCTION users.create_user(
    i_password TEXT,
    i_first_name VARCHAR(50),
    i_last_name VARCHAR(50),
    i_phone_number VARCHAR(15)
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'users.user_id_seq';
    o_id BIGINT;
BEGIN
    INSERT INTO users."user"(
        id,
        first_name,
        last_name,
        phone_number
    ) VALUES (
         nextval(id_seq_name),
         i_first_name,
         i_last_name,
         i_phone_number
     )
    RETURNING id INTO o_id;

    INSERT INTO users.user_password(user_id, password) VALUES (o_id, i_password);

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
