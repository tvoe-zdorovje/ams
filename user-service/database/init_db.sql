CREATE DATABASE user_db;

\c user_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;


-- init user-service infrastructure


CREATE SCHEMA IF NOT EXISTS users;


-- init users & roles


-- ussliquibase (crete only)


CREATE USER ussliquibase WITH PASSWORD 'ussliquibase';

GRANT CONNECT, CREATE
    ON DATABASE user_db
    TO ussliquibase;
GRANT ALL
    ON SCHEMA public
    TO ussliquibase;
GRANT USAGE, CREATE
    ON SCHEMA users
    TO ussliquibase;

REVOKE GRANT OPTION FOR ALL
    ON DATABASE user_db
    FROM ussliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    REVOKE SELECT, INSERT, UPDATE, DELETE
    ON TABLES
    FROM ussliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    REVOKE EXECUTE
    ON FUNCTIONS
    FROM ussliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    REVOKE SELECT,
    UPDATE ON SEQUENCES
    FROM ussliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO ussliquibase;

-- allows the liquibase user to use 'SET ROLE <superuser>'
-- that's required to change an owner of a table
DO $$
    BEGIN
        EXECUTE format('GRANT %I TO %I', current_user, 'ussliquibase');
    END $$;


-- ussportal (read only & execute procedures and functions)


CREATE USER ussportal WITH PASSWORD 'ussportal';

GRANT CONNECT
    ON DATABASE user_db
    TO ussportal;
GRANT USAGE
    ON SCHEMA users
    TO ussportal;

ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    IN SCHEMA users
    GRANT SELECT
    ON TABLES TO ussportal;
ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    IN SCHEMA users
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES
    ON TABLES TO ussportal;
ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    IN SCHEMA users
    GRANT EXECUTE
    ON FUNCTIONS TO ussportal;


-- adsportal_fdw (read only)


CREATE USER adsportal_fdw WITH PASSWORD 'adsportal_fdw';


GRANT CONNECT
    ON DATABASE user_db
    TO adsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA users
    TO adsportal_fdw;
GRANT USAGE
    ON SCHEMA users
    TO adsportal_fdw;

ALTER DEFAULT PRIVILEGES
    FOR USER ussliquibase
    IN SCHEMA users
    GRANT SELECT
    ON TABLES TO adsportal_fdw;


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
