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
GRANT SELECT
    ON ALL TABLES IN SCHEMA administration, fdw
    TO adsportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA administration
    TO adsportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA administration
    TO adsportal;
GRANT USAGE
    ON FOREIGN SERVER brand_fdw_db, studio_fdw_db, user_fdw_db
    TO adsportal;
GRANT USAGE
    ON SCHEMA fdw
    TO adsportal;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA administration, fdw
    GRANT SELECT
    ON TABLES TO adsportal;


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
