CREATE SCHEMA IF NOT EXISTS administration;


CREATE TABLE IF NOT EXISTS administration.role(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS administration.permission(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS administration.role_permissions(
    role_id INT,
    permission_id INT,
    CONSTRAINT fk_role_permissions_role_id
        FOREIGN KEY (role_id)
        REFERENCES administration.role (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission_id
        FOREIGN KEY (permission_id)
        REFERENCES administration.permission (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS administration.brand_roles(
    brand_id INT,
    permission_id INT,
    CONSTRAINT fk_brand_roles_brand_id
        FOREIGN KEY (brand_id)
            REFERENCES fdw.brand (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_brand_roles_permission_id
        FOREIGN KEY (permission_id)
            REFERENCES administration.permission (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS administration.studio_roles(
    studio_id INT,
    permission_id INT,
    CONSTRAINT fk_studio_roles_studio_id
      FOREIGN KEY (studio_id)
          REFERENCES fdw.studio (id)
          ON DELETE CASCADE,
    CONSTRAINT fk_studio_roles_permission_id
      FOREIGN KEY (permission_id)
          REFERENCES administration.permission (id)
          ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS administration.user_roles(
    user_id INT,
    permission_id INT,
    CONSTRAINT fk_user_roles_user_id
        FOREIGN KEY (user_id)
            REFERENCES fdw.user (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_permission_id
        FOREIGN KEY (permission_id)
            REFERENCES administration.permission (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS administration.brand_studios(
    brand_id INT,
    studio_id INT,
    CONSTRAINT fk_brand_studios_brand_id
       FOREIGN KEY (brand_id)
           REFERENCES fdw.brand (id)
           ON DELETE CASCADE,
    CONSTRAINT fk_brand_studios_studio_id
       FOREIGN KEY (studio_id)
           REFERENCES fdw.studio (id)
           ON DELETE CASCADE
);
