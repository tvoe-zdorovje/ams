CREATE DATABASE administration_db;

\c administration_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;


CREATE USER adsliquibase WITH PASSWORD 'adsliquibase';


GRANT CONNECT, CREATE
    ON DATABASE administration_db
    TO adsliquibase;
GRANT ALL PRIVILEGES
    ON SCHEMA public
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
    REVOKE SELECT,
    UPDATE ON SEQUENCES
    FROM adsliquibase;

ALTER DEFAULT PRIVILEGES
    FOR USER adsliquibase
    IN SCHEMA public
    GRANT ALL PRIVILEGES
    ON TABLES
    TO adsliquibase;


-- init administration-service infrastructure


CREATE SCHEMA IF NOT EXISTS administration;


GRANT USAGE, CREATE
    ON SCHEMA administration
    TO adsliquibase;


CREATE USER adsportal WITH PASSWORD 'adsportal';


GRANT CONNECT
    ON DATABASE administration_db
    TO adsportal;
GRANT SELECT
    ON ALL TABLES IN SCHEMA administration
    TO adsportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA administration
    TO adsportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA administration
    TO adsportal;


-- init FDW infrastructure


CREATE SCHEMA IF NOT EXISTS fdw;


GRANT SELECT
    ON ALL TABLES IN SCHEMA fdw
    TO adsportal;


CREATE EXTENSION IF NOT EXISTS postgres_fdw;


CREATE SERVER user_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'user_db', dbname 'user_db', port '5432');

CREATE USER MAPPING FOR adsportal SERVER user_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');


CREATE SERVER brand_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'brand_db', dbname 'brand_db', port '5432');

CREATE USER MAPPING FOR adsportal SERVER brand_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');


CREATE SERVER studio_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'studio_db', dbname 'studio_db', port '5432');

CREATE USER MAPPING FOR adsportal SERVER studio_fdw_db
    OPTIONS (user 'adsportal_fdw', password 'adsportal_fdw');


-- init other users


CREATE USER apsportal_fdw WITH PASSWORD 'apsportal_fdw';


GRANT CONNECT
    ON DATABASE administration_db
    TO apsportal_fdw;
GRANT SELECT
    ON ALL TABLES IN SCHEMA administration
    TO apsportal_fdw;
