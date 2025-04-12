CREATE TABLE IF NOT EXISTS administration.role(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS administration.permission(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS administration.role_permissions(
    role_id BIGINT,
    permission_id BIGINT,
    CONSTRAINT fk_role_permissions_role_id
        FOREIGN KEY (role_id)
        REFERENCES administration.role (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission_id
        FOREIGN KEY (permission_id)
        REFERENCES administration.permission (id)
        ON DELETE CASCADE,
    CONSTRAINT unique_role_id_permission_id UNIQUE (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS administration.brand_roles(
    brand_id BIGINT,
    role_id BIGINT,
    CONSTRAINT fk_brand_roles_brand_id
        FOREIGN KEY (brand_id)
            REFERENCES administration.brand (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_brand_roles_role_id
        FOREIGN KEY (role_id)
            REFERENCES administration.role (id)
            ON DELETE CASCADE,
    CONSTRAINT unique_brand_id_role_id UNIQUE (brand_id, role_id)
);

CREATE TABLE IF NOT EXISTS administration.studio_roles(
    studio_id BIGINT,
    role_id BIGINT,
    CONSTRAINT fk_studio_roles_studio_id
      FOREIGN KEY (studio_id)
          REFERENCES administration.studio (id)
          ON DELETE CASCADE,
    CONSTRAINT fk_studio_roles_role_id
      FOREIGN KEY (role_id)
          REFERENCES administration.role (id)
          ON DELETE CASCADE,
    CONSTRAINT unique_studio_id_role_id UNIQUE (studio_id, role_id)
);

CREATE TABLE IF NOT EXISTS administration.user_roles(
    user_id BIGINT,
    role_id BIGINT,
    CONSTRAINT fk_user_roles_user_id
        FOREIGN KEY (user_id)
            REFERENCES administration.user (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role_id
        FOREIGN KEY (role_id)
            REFERENCES administration.role (id)
            ON DELETE CASCADE,
    CONSTRAINT unique_user_id_role_id UNIQUE (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS administration.brand_studios(
    brand_id BIGINT,
    studio_id BIGINT UNIQUE,
    CONSTRAINT fk_brand_studios_brand_id
       FOREIGN KEY (brand_id)
           REFERENCES administration.brand (id)
           ON DELETE CASCADE,
    CONSTRAINT fk_brand_studios_studio_id
       FOREIGN KEY (studio_id)
           REFERENCES administration.studio (id)
           ON DELETE CASCADE
);
