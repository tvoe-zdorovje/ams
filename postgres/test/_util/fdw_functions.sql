SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.has_server(
    server_name TEXT,
    server_host TEXT,
    server_dbname TEXT,
    server_port TEXT
) RETURNS SETOF TEXT AS $$
    DECLARE
        server RECORD := _get_server(server_name);
BEGIN
    RETURN QUERY SELECT ok(
        server IS NOT NULL,
        format('server "%s" must exist', UPPER(server_name))
    );
    RETURN QUERY SELECT is(
        server.host,
        server_host,
        format('server "%s" must have host "%s"', UPPER(server_name), server_host)
    );
    RETURN QUERY SELECT is(
        server.dbname,
        server_dbname,
        format('server "%s" must have dbname "%s"', UPPER(server_name), server_dbname)
    );
    RETURN QUERY SELECT is(
        server.port,
        server_port,
        format('server "%s" must have port "%s"', UPPER(server_name), server_port)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.has_user_mapping(
    server_name NAME,
    local_user_name NAME,
    remote_user_name TEXT
) RETURNS SETOF TEXT AS $$
DECLARE
    user_mapping RECORD := _get_user_mapping(server_name);
BEGIN
    RETURN QUERY SELECT ok(
        user_mapping IS NOT NULL,
        format('user mapping for "%s" server must exist', UPPER(server_name))
    );
    RETURN QUERY SELECT is(
        user_mapping.server_name,
        server_name,
        format('user mapping for "%s" server must have "%s" server name', UPPER(server_name), server_name)
    );
    RETURN QUERY SELECT is(
        user_mapping.local_user_name,
        local_user_name,
        format('user mapping for "%s" server must have "%s" local user name', UPPER(server_name), local_user_name)
    );
    RETURN QUERY SELECT is(
        user_mapping.remote_user_name,
        remote_user_name,
        format('user mapping for "%s" server must have "%s" remote user name', UPPER(server_name), remote_user_name)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests._get_server(
    server_name NAME
) RETURNS TABLE(
    host TEXT,
    dbname TEXT,
    port TEXT
) AS $$
BEGIN
    RETURN QUERY SELECT
        MAX(CASE WHEN key = 'host' THEN value END) AS host,
        MAX(CASE WHEN key = 'dbname' THEN value END) AS dbname,
        MAX(CASE WHEN key = 'port' THEN value END) AS port
    FROM (
        SELECT
            split_part(option, '=', 1) AS key,
            split_part(option, '=', 2) AS value
        FROM unnest(
            (SELECT srvoptions FROM pg_foreign_server WHERE srvname = server_name)
        ) AS option
    ) AS parsed_options;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests._get_user_mapping(
    _server_name NAME
) RETURNS TABLE(
    server_name NAME,
    local_user_name NAME,
    remote_user_name TEXT
) AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_rel_owner('pg_user_mappings'));

    RETURN QUERY SELECT
        user_mapping.srvname,
        user_mapping.usename AS local_user_name,
        MAX(CASE WHEN user_mapping.key LIKE 'user' THEN value END) AS remote_user_name
    FROM (
        SELECT
            srvname,
            usename,
            split_part(options, '=', 1) AS key,
            split_part(options, '=', 2) AS value
        FROM (
            SELECT srvname, usename, UNNEST(umoptions) AS options FROM pg_user_mappings
        ) AS pum
        WHERE options LIKE 'user=%'
    ) AS user_mapping
    WHERE user_mapping.srvname = _server_name
    GROUP BY user_mapping.srvname, user_mapping.usename
    ;
END;
$$ LANGUAGE plpgsql;


select plan(3);
select has_function('tests'::name, 'has_server'::name);
select has_function('tests'::name, '_get_server'::name);
select has_function('tests'::name, '_get_user_mapping'::name);
select finish(true);
