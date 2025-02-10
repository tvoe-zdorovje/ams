CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.role(
    id INT,
    name VARCHAR(100),
    description VARCHAR(255)
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'role');

CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.permission(
    id INT,
    name VARCHAR(100),
    description VARCHAR(255)
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'permission');

CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.role_permissions(
    role_id INT,
    permission_id INT
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'role_permissions');

CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.brand_roles(
    brand_id INT,
    role_id INT
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'brand_roles');

CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.studio_roles(
    studio_id INT,
    role_id INT
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'studio_roles');

CREATE FOREIGN TABLE IF NOT EXISTS administration_fdw.user_roles(
    user_id INT,
    role_id INT
)
    SERVER administration_fdw_db
    OPTIONS (schema_name 'administration', table_name 'user_roles');
