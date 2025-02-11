CREATE DATABASE studio_db;

\c studio_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;


-- init studio-service infrastructure


CREATE SCHEMA IF NOT EXISTS studios;


-- init users & roles


-- stsliquibase (crete only)


CREATE USER stsliquibase WITH PASSWORD 'stsliquibase';

GRANT CONNECT, CREATE
    ON DATABASE studio_db
    TO stsliquibase;
GRANT ALL
    ON SCHEMA public
    TO stsliquibase;
GRANT USAGE, CREATE
    ON SCHEMA studios
    TO stsliquibase;

REVOKE GRANT OPTION FOR ALL
    ON DATABASE studio_db
    FROM stsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    REVOKE SELECT, INSERT, UPDATE, DELETE
    ON TABLES
    FROM stsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    REVOKE EXECUTE
    ON FUNCTIONS
    FROM stsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    REVOKE SELECT,
    UPDATE ON SEQUENCES
    FROM stsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO stsliquibase;

-- allows the liquibase user to use 'SET ROLE <superuser>'
-- that's required to change an owner of a table
DO $$
    BEGIN
        EXECUTE format('GRANT %I TO %I', current_user, 'stsliquibase');
    END $$;


-- stsportal (read only & execute procedures and functions)


CREATE USER stsportal WITH PASSWORD 'stsportal';


GRANT CONNECT
    ON DATABASE studio_db
    TO stsportal;
GRANT USAGE
    ON SCHEMA studios
    TO stsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    IN SCHEMA studios
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES
    ON TABLES TO stsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    IN SCHEMA studios
    GRANT EXECUTE
    ON FUNCTIONS TO stsportal;


-- adsportal_fdw (read only)


CREATE USER adsportal_fdw WITH PASSWORD 'adsportal_fdw';


GRANT CONNECT
    ON DATABASE studio_db
    TO adsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA studios
    TO adsportal_fdw;
GRANT USAGE
    ON SCHEMA studios
    TO adsportal_fdw;

ALTER DEFAULT PRIVILEGES
    FOR USER stsliquibase
    IN SCHEMA studios
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
