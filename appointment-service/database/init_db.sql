CREATE DATABASE appointment_db;

\c appointment_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;


-- init appointment-service infrastructure


CREATE SCHEMA IF NOT EXISTS appointments;


-- init FDW infrastructure


CREATE SCHEMA IF NOT EXISTS administration_fdw;


CREATE EXTENSION IF NOT EXISTS postgres_fdw;


CREATE SERVER administration_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'administration_db', dbname 'administration_db', port '5432');


-- init users & roles


-- apsliquibase (crete only)


CREATE USER apsliquibase WITH PASSWORD 'apsliquibase';

GRANT CONNECT, CREATE
    ON DATABASE appointment_db
    TO apsliquibase;
GRANT ALL
    ON SCHEMA public
    TO apsliquibase;
GRANT USAGE, CREATE
    ON SCHEMA appointments, administration_fdw
    TO apsliquibase;
GRANT USAGE
    ON FOREIGN SERVER administration_fdw_db
    TO apsliquibase;

REVOKE GRANT OPTION FOR ALL
    ON DATABASE appointment_db
    FROM apsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    REVOKE SELECT, INSERT, UPDATE, DELETE
    ON TABLES
    FROM apsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    REVOKE EXECUTE
    ON FUNCTIONS
    FROM apsliquibase;
ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    REVOKE SELECT,
    UPDATE ON SEQUENCES
    FROM apsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO apsliquibase;

-- allows the liquibase user to use 'SET ROLE <superuser>'
-- that's required to change an owner of a table
DO $$
    BEGIN
        EXECUTE format('GRANT %I TO %I', current_user, 'apsliquibase');
    END $$;


-- apsportal (read only & execute procedures and functions)


CREATE USER apsportal WITH PASSWORD 'apsportal';

GRANT CONNECT
    ON DATABASE administration_db
    TO apsportal;
GRANT USAGE
    ON FOREIGN SERVER administration_fdw_db
    TO apsportal;
GRANT USAGE
    ON SCHEMA appointments, administration_fdw
    TO apsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase, adsliquibase
    IN SCHEMA appointments, administration_fdw
    GRANT SELECT
    ON TABLES TO apsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    IN SCHEMA appointments
    GRANT INSERT, UPDATE, DELETE, REFERENCES
    ON TABLES TO apsportal;
ALTER DEFAULT PRIVILEGES
    FOR USER apsliquibase
    IN SCHEMA appointments
    GRANT EXECUTE
    ON FUNCTIONS TO apsportal;

CREATE USER MAPPING FOR apsportal SERVER administration_fdw_db
    OPTIONS (user 'apsportal_fdw', password 'apsportal_fdw');


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
