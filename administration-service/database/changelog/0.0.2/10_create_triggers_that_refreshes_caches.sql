CREATE OR REPLACE FUNCTION administration.refresh_fdw() RETURNS TRIGGER AS $$
BEGIN
    FOR i IN 0..TG_NARGS-1 LOOP
        CASE TG_ARGV[i]
            WHEN 'user' THEN PERFORM administration.refresh_fdw_user();
            WHEN 'brand' THEN PERFORM administration.refresh_fdw_brand();
            WHEN 'studio' THEN PERFORM administration.refresh_fdw_studio();
            END CASE;
        END LOOP;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER refresh_user_cache_trg
    BEFORE INSERT ON administration.user_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw('user');

CREATE TRIGGER refresh_brand_cache_trg
    BEFORE INSERT ON administration.brand_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw('brand');

CREATE TRIGGER refresh_studio_cache_trg
    BEFORE INSERT ON administration.studio_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw('studio');

CREATE TRIGGER refresh_brand_studio_cache_trg
    BEFORE INSERT ON administration.brand_studios
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw('brand', 'studio');
