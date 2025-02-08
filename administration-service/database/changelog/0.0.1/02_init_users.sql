CREATE USER adsportal WITH PASSWORD 'adsportal'; -- FIXME sensitive

GRANT SELECT
    ON ALL TABLES IN SCHEMA fdw
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

CREATE USER apsportal_liquibase WITH PASSWORD 'apsportal_liquibase';

GRANT CREATE
    ON DATABASE administration_db
    TO apsportal_liquibase;

REVOKE ALL PRIVILEGES
    ON SCHEMA administration
    FROM apsportal_liquibase;
