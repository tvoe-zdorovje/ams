SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_administration_fdw_db_server_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT has_server(
        server_name := 'administration_fdw_db',
        server_host := 'administration_db',
        server_dbname := 'administration_db',
        server_port := '5432'
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_administration_fdw_db_server_user_mapping() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT has_user_mapping(
        server_name := 'administration_fdw_db',
        local_user_name := 'apsportal',
        remote_user_name := 'apsportal_fdw'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_administration_fdw_db_server_structure');
select has_function('test_administration_fdw_db_server_user_mapping');
select finish(true);
