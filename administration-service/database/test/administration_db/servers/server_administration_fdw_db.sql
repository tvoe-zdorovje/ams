SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_fdw_servers_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT has_server(
        server_name := 'user_fdw_db',
        server_host := 'user_db',
        server_dbname := 'user_db',
        server_port := '5432'
    );
    RETURN QUERY SELECT has_server(
        server_name := 'brand_fdw_db',
        server_host := 'brand_db',
        server_dbname := 'brand_db',
        server_port := '5432'
    );
    RETURN QUERY SELECT has_server(
        server_name := 'studio_fdw_db',
        server_host := 'studio_db',
        server_dbname := 'studio_db',
        server_port := '5432'
    );

END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_fdw_servers_user_mappings() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT has_user_mapping(
        server_name := 'user_fdw_db',
        local_user_name := 'adsportal',
        remote_user_name := 'adsportal_fdw'
    );
    RETURN QUERY SELECT has_user_mapping(
        server_name := 'brand_fdw_db',
        local_user_name := 'adsportal',
        remote_user_name := 'adsportal_fdw'
    );
    RETURN QUERY SELECT has_user_mapping(
        server_name := 'studio_fdw_db',
        local_user_name := 'adsportal',
        remote_user_name := 'adsportal_fdw'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_fdw_servers_structure');
select has_function('test_fdw_servers_user_mappings');
select finish(true);
