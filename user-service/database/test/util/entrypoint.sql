CREATE SCHEMA IF NOT EXISTS tap;
CREATE SCHEMA IF NOT EXISTS tests;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, tests, users, public;


CREATE OR REPLACE FUNCTION public.runtests() RETURNS SETOF text AS
$$
BEGIN
    SET search_path TO tap, tests, users, public;

    RETURN QUERY SELECT * FROM tap.runtests('^test');

    DROP SCHEMA IF EXISTS tap CASCADE;
    DROP SCHEMA IF EXISTS tests CASCADE;

    DROP FUNCTION IF EXISTS public.runtests();
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('public'::name, 'runtests'::name);
select finish(true);
