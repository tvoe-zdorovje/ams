CREATE USER apsportal WITH PASSWORD 'apsportal'; -- FIXME sensitive

GRANT SELECT
    ON ALL TABLES IN SCHEMA fdw
    TO apsportal;
GRANT SELECT
    ON ALL TABLES IN SCHEMA appointments
    TO apsportal;
GRANT EXECUTE
    ON ALL FUNCTIONS IN SCHEMA appointments
    TO apsportal;
GRANT EXECUTE
    ON ALL PROCEDURES IN SCHEMA appointments
    TO apsportal;
