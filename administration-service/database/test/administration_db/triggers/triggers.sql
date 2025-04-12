SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_change_table_owner_trigger() RETURNS SETOF TEXT AS $$
DECLARE
    schema_owner TEXT;
BEGIN
    SELECT _get_schema_owner('public') INTO schema_owner;
    assert schema_owner != current_user;

    CREATE TABLE public.test_table(id INT);

    RETURN NEXT table_owner_is(
        'public',
        'test_table',
        schema_owner,
        'trigger "CHANGE_TABLE_OWNER" must change a table owner to a schema owner'
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_refresh_user_cache_trg_trigger() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    PREPARE user_cache AS INSERT INTO administration.user_roles(user_id, role_id) VALUES (-123, -321);
    RETURN NEXT throws_like(
        'user_cache',
        'user mapping not found for user "%", server "%"'
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_refresh_brand_cache_trg_trigger() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    PREPARE brand_cache AS INSERT INTO administration.brand_roles(brand_id, role_id) VALUES (-123, -321);
    RETURN NEXT throws_like(
        'brand_cache',
        'user mapping not found for user "%", server "%"'
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_refresh_studio_cache_trg_trigger() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    PREPARE studio_cache AS INSERT INTO administration.studio_roles(studio_id, role_id) VALUES (-123, -321);
    RETURN NEXT throws_like(
        'studio_cache',
        'user mapping not found for user "%", server "%"'
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_refresh_brand_studio_cache_trg_trigger() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));


    PREPARE brand_studio_cache AS INSERT INTO administration.brand_studios(brand_id, studio_id) VALUES (-123, -321);
    RETURN NEXT throws_like(
        'brand_studio_cache',
        'user mapping not found for user "%", server "%"'
    );
END;
$$ LANGUAGE plpgsql;


select plan(5);
select has_function('test_change_table_owner_trigger');
select has_function('test_refresh_user_cache_trg_trigger');
select has_function('test_refresh_brand_cache_trg_trigger');
select has_function('test_refresh_studio_cache_trg_trigger');
select has_function('test_refresh_brand_studio_cache_trg_trigger');
select finish(true);
