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
