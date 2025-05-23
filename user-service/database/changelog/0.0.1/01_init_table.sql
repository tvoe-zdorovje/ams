CREATE TABLE IF NOT EXISTS users.user(
    id BIGINT PRIMARY KEY NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,

    CONSTRAINT phone_number_e164
        CHECK (phone_number ~ '^\+[1-9]\d{1,14}$')
);
