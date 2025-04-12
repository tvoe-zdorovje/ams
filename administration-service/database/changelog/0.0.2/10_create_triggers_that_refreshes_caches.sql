CREATE TRIGGER refresh_user_cache_trg
    BEFORE INSERT ON administration.user_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw_user();

CREATE TRIGGER refresh_brand_cache_trg
    BEFORE INSERT ON administration.brand_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw_brand();

CREATE TRIGGER refresh_studio_cache_trg
    BEFORE INSERT ON administration.studio_roles
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw_studio();


CREATE OR REPLACE FUNCTION administration.refresh_fdw(
    entities TEXT[]
) RETURNS VOID AS $$
DECLARE
    entity TEXT;
BEGIN
    FOR entity IN entities LOOP
        CASE entity
            WHEN 'user' THEN PERFORM administration.refresh_fdw_user();
            WHEN 'brand' THEN PERFORM administration.refresh_fdw_brand();
            WHEN 'studio' THEN PERFORM administration.refresh_fdw_studio();
        END CASE;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER refresh_brand_studio_cache_trg
    BEFORE INSERT ON administration.brand_studios
    FOR EACH STATEMENT
EXECUTE FUNCTION administration.refresh_fdw(['brand', 'studio']);