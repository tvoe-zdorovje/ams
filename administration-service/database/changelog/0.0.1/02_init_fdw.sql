CREATE EXTENSION IF NOT EXISTS postgres_fdw;


CREATE SERVER user_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'user_db', dbname 'user_db', port '5432');

CREATE SERVER brand_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'brand_db', dbname 'brand_db', port '5432');

CREATE SERVER studio_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'studio_db', dbname 'studio_db', port '5432');

-- TODO create read-only user
CREATE USER MAPPING FOR CURRENT_USER SERVER user_fdw_db
    OPTIONS (user 'user', password 'pass');

CREATE USER MAPPING FOR CURRENT_USER SERVER brand_fdw_db
    OPTIONS (user 'user', password 'pass');

CREATE USER MAPPING FOR CURRENT_USER SERVER studio_fdw_db
    OPTIONS (user 'user', password 'pass');
