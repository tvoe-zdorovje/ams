CREATE DATABASE studio_db;

\c studio_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;

CREATE USER stsliquibase WITH PASSWORD 'stsliquibase';

GRANT CONNECT, CREATE
    ON DATABASE studio_db
    TO stsliquibase;
GRANT ALL
    ON SCHEMA public
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


-- init studio-service infrastructure


CREATE SCHEMA IF NOT EXISTS studios;


GRANT USAGE, CREATE
    ON SCHEMA studios
    TO stsliquibase;


CREATE USER stssportal WITH PASSWORD 'stssportal';


GRANT SELECT
    ON ALL TABLES IN SCHEMA studios
    TO stssportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA studios
    TO stssportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA studios
    TO stssportal;


-- init other users


CREATE USER adsportal_fdw WITH PASSWORD 'adsportal_fdw';


GRANT CONNECT
    ON DATABASE studio_db
    TO adsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA studios
    TO adsportal_fdw;
