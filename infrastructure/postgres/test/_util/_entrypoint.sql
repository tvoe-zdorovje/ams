CREATE SCHEMA IF NOT EXISTS tap;
CREATE SCHEMA IF NOT EXISTS tests;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION public.runtests() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE 'SET search_path TO ' || (
        SELECT string_agg(nspname, ', ')
        FROM pg_catalog.pg_namespace
        WHERE nspname NOT LIKE 'pg_%' AND nspname <> 'information_schema'
    );

    RETURN QUERY SELECT * FROM tap.runtests('^test');

    DROP SCHEMA IF EXISTS tap CASCADE;
    DROP SCHEMA IF EXISTS tests CASCADE;

    DROP FUNCTION IF EXISTS public.runtests();
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('public'::name, 'runtests'::name);
select finish(true);
