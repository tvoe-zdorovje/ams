CREATE DATABASE brand_db;

\c brand_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXIbrs public;


-- init brand-service infrastructure


CREATE SCHEMA IF NOT EXIbrs brands;


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


-- brsportal (read only & execute procedures and functions)


CREATE USER brsportal WITH PASSWORD 'brsportal';


GRANT SELECT
    ON ALL TABLES IN SCHEMA brands
    TO brsportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA brands
    TO brsportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA brands
    TO brsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
    GRANT SELECT
    ON TABLES TO brsportal;


-- adsportal_fdw (read only)


CREATE USER adsportal_fdw WITH PASSWORD 'adsportal_fdw';


GRANT CONNECT
    ON DATABASE brand_db
    TO adsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA brands
    TO adsportal_fdw;

ALTER DEFAULT PRIVILEGES
    FOR USER brsliquibase
    IN SCHEMA brands
    GRANT SELECT
    ON TABLES TO adsportal_fdw;
