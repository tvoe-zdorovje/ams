DROP TABLE users.user_password;

ALTER TABLE users."user"
    ADD COLUMN idp_uuid VARCHAR(36) UNIQUE;

DROP FUNCTION users.create_user(i_password TEXT, i_first_name VARCHAR, i_last_name VARCHAR, i_phone_number VARCHAR);

CREATE OR REPLACE FUNCTION users.create_user(
    i_idp_uuid VARCHAR(36),
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
    phone_number,
    idp_uuid
) VALUES (
    nextval(id_seq_name),
    i_first_name,
    i_last_name,
    i_phone_number,
    i_idp_uuid
)
    RETURNING id INTO o_id;

RETURN o_id;
END;
$$ LANGUAGE plpgsql;
