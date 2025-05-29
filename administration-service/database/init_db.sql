CREATE DATABASE administration_db;

\c administration_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;


-- init administration-service infrastructure


CREATE SCHEMA IF NOT EXISTS administration;


-- init FDW infrastructure


CREATE EXTENSION IF NOT EXISTS postgres_fdw;


CREATE SERVER user_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'user_db', dbname 'user_db', port '5432');

CREATE SERVER brand_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'brand_db', dbname 'brand_db', port '5432');

CREATE SERVER studio_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'studio_db', dbname 'studio_db', port '5432');


CREATE SCHEMA IF NOT EXISTS fdw;


-- init users & roles


-- adsliquibase (crete only)


CREATE USER adsliquibase WITH PASSWORD 'adsliquibase';


GRANT CONNECT, CREATE
    ON DATABASE administration_db
    TO adsliquibase;
GRANT ALL PRIVILEGES
    ON SCHEMA public
    TO adsliquibase;
GRANT USAGE, CREATE
    ON SCHEMA administration
    TO adsliquibase;
GRANT USAGE, CREATE
    ON SCHEMA fdw
    TO adsliquibase;
GRANT USAGE
    ON FOREIGN SERVER brand_fdw_db, studio_fdw_db, user_fdw_db
    TO adsliquibase;


REVOKE GRANT OPTION FOR ALL PRIVILEGES
    ON DATABASE administration_db
    FROM adsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    REVOKE SELECT, INSERT, UPDATE, DELETE
    ON TABLES
    FROM adsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    REVOKE EXECUTE
    ON FUNCTIONS
    FROM adsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    REVOKE SELECT, UPDATE
    ON SEQUENCES
    FROM adsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO adsliquibase;

-- allows the liquibase user to use 'SET ROLE <superuser>'
-- that's required to change an owner of a table
DO $$
    BEGIN
        EXECUTE format('GRANT %I TO %I', current_user, 'adsliquibase');
    END $$;


-- adsportal (read only & execute procedures and functions)


CREATE USER adsportal WITH PASSWORD 'adsportal';


CREATE USER MAPPING FOR adsportal SERVER user_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');

CREATE USER MAPPING FOR adsportal SERVER brand_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');

CREATE USER MAPPING FOR adsportal SERVER studio_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');


GRANT CONNECT
    ON DATABASE administration_db
    TO adsportal;
GRANT USAGE
    ON FOREIGN SERVER brand_fdw_db, studio_fdw_db, user_fdw_db
    TO adsportal;
GRANT USAGE
    ON SCHEMA administration, fdw
    TO adsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA fdw
    GRANT SELECT
    ON TABLES TO adsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration
    GRANT SELECT, UPDATE
    ON SEQUENCES TO adsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES
    ON TABLES TO adsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration
    GRANT EXECUTE
    ON FUNCTIONS TO adsportal;


-- apsportal_fdw (read only)


CREATE USER apsportal_fdw WITH PASSWORD 'apsportal_fdw';


GRANT CONNECT
    ON DATABASE administration_db
    TO apsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA administration
    TO apsportal_fdw;
GRANT USAGE
    ON SCHEMA administration
    TO apsportal_fdw;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration
    GRANT SELECT
    ON TABLES TO apsportal_fdw;


-- ausportal (read only)


CREATE USER ausportal WITH PASSWORD 'ausportal';


GRANT CONNECT
    ON DATABASE administration_db
    TO ausportal;
GRANT SELECT
    ON ALL TABLES IN SCHEMA administration
    TO ausportal;
GRANT USAGE
    ON SCHEMA administration
    TO ausportal;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration
    GRANT SELECT
    ON TABLES TO ausportal;


 -- change owner trigger
 -- Foreign keys work on behalf of the table owner.
 -- To avoid granting additional privileges to the liquibase user,
 -- this trigger changes the table owner to the schema owner.


CREATE OR REPLACE FUNCTION change_owner()
    RETURNS event_trigger AS $$
DECLARE
    obj record;
    schema_owner TEXT;
BEGIN
    FOR obj IN
        SELECT schema_name, object_identity
        FROM pg_event_trigger_ddl_commands()
        WHERE command_tag = 'CREATE TABLE'
        LOOP
            SELECT pg_catalog.pg_get_userbyid(nspowner)
            INTO schema_owner
            FROM pg_namespace
            WHERE nspname = obj.schema_name;

            RAISE LOG 'Execute: ALTER TABLE IF EXISTS % OWNER TO %', obj.object_identity, schema_owner;

            IF schema_owner IS NOT NULL THEN
                EXECUTE format(
                    'ALTER TABLE IF EXISTS %s OWNER TO %I',
                    obj.object_identity,
                    schema_owner
                );
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE EVENT TRIGGER change_table_owner
    ON ddl_command_end
    WHEN TAG IN ('CREATE TABLE', 'CREATE TABLE AS')
EXECUTE FUNCTION change_owner();
