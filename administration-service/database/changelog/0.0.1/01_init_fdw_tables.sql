CREATE FOREIGN TABLE IF NOT EXISTS fdw.user(
    id BIGINT
)
    SERVER user_fdw_db
    OPTIONS (schema_name 'users', table_name 'user');

CREATE FOREIGN TABLE IF NOT EXISTS fdw.brand(
    id BIGINT
)
    SERVER brand_fdw_db
    OPTIONS (schema_name 'brands', table_name 'brand');

CREATE FOREIGN TABLE IF NOT EXISTS fdw.studio(
    id BIGINT
)
    SERVER studio_fdw_db
    OPTIONS (schema_name 'studios', table_name 'studio');
