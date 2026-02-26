CREATE TABLE IF NOT EXISTS users.user_password(
    user_id BIGINT NOT NULL,
    password TEXT NOT NULL,

    CONSTRAINT fk_user_password_user_id
        FOREIGN KEY (user_id)
            REFERENCES users."user"(id)
            ON DELETE CASCADE
);
