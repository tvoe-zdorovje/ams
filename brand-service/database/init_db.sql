CREATE DATABASE brand_db;

\c brand_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT exists public;


-- init brand-service infrastructure


CREATE SCHEMA IF NOT exists brands;


-- init users & roles


-- brsliquibase (crete only)


CREATE USER brsliquibase WITH PASSWORD 'brsliquibase';

GRANT CONNECT, CREATE
    ON DATABASE brand_db
    TO brsliquibase;
GRANT ALL
    ON SCHEMA public
    TO brsliquibase;
GRANT USAGE, CREATE
    ON SCHEMA brands
    TO brsliquibase;

REVOKE GRANT OPTION FOR ALL
    ON DATABASE brand_db
    FROM brsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    REVOKE SELECT, INSERT, UPDATE, DELETE
    ON TABLES
    FROM brsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    REVOKE EXECUTE
    ON FUNCTIONS
    FROM brsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    REVOKE SELECT,
    UPDATE ON SEQUENCES
    FROM brsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO brsliquibase;

-- allows the liquibase user to use 'SET ROLE <superuser>'
-- that's required to change an owner of a table
DO $$
    BEGIN
        EXECUTE format('GRANT %I TO %I', current_user, 'brsliquibase');
    END $$;


-- brsportal (read only & execute procedures and functions)


CREATE USER brsportal WITH PASSWORD 'brsportal';


GRANT CONNECT
    ON DATABASE brand_db
    TO brsportal;
GRANT USAGE
    ON SCHEMA brands
    TO brsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
    GRANT SELECT, UPDATE
    ON SEQUENCES TO brsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES
    ON TABLES TO brsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
    GRANT EXECUTE
    ON FUNCTIONS TO brsportal;


-- adsportal_fdw (read only)


CREATE USER adsportal_fdw WITH PASSWORD 'adsportal_fdw';


GRANT CONNECT
    ON DATABASE brand_db
    TO adsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA brands
    TO adsportal_fdw;
GRANT USAGE
    ON SCHEMA brands
    TO adsportal_fdw;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
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
