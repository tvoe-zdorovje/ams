CREATE DATABASE appointment_db;

\c appointment_db;


-- init liquibase infrastructure


CREATE SCHEMA IF NOT EXISTS public;

CREATE USER apsliquibase WITH PASSWORD 'apsliquibase';

GRANT CONNECT, CREATE
    ON DATABASE appointment_db
    TO apsliquibase;
GRANT ALL
    ON SCHEMA public
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


-- init appointment-service infrastructure


CREATE SCHEMA IF NOT EXISTS appointments;


CREATE USER apsportal WITH PASSWORD 'apsportal';


GRANT SELECT
    ON ALL TABLES IN SCHEMA appointments
    TO apsportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA appointments
    TO apsportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA appointments
    TO apsportal;


-- init FDW infrastructure


CREATE SCHEMA IF NOT EXISTS administration_fdw;


GRANT SELECT
    ON ALL TABLES IN SCHEMA administration_fdw
    TO apsportal;


CREATE EXTENSION IF NOT EXISTS postgres_fdw;


CREATE SERVER administration_fdw_db FOREIGN DATA WRAPPER postgres_fdw
    OPTIONS (host 'administration_db', dbname 'administration_db', port '5432');

CREATE USER MAPPING FOR apsportal SERVER administration_fdw_db
    OPTIONS (user 'apsportal_fdw', password 'apsportal_fdw');
