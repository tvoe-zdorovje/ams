SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_add_brand_studios_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'administration';
    routine_name TEXT := 'add_brand_studios';
BEGIN
    RETURN NEXT has_function(
        schema_name,
        routine_name,
        format('routine "%s" must exist', routine_name)
    );

    RETURN NEXT is_normal_function(
        schema_name,
        routine_name,
        format('routine "%s" must be a normal function', routine_name)
    );

    RETURN NEXT function_returns(
        schema_name,
        routine_name,
        'BIGINT',
        format('routine "%s" must have "BIGINT" return type', routine_name)
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tests.test_add_brand_studios_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'administration.add_brand_studios';

    brand_id BIGINT := -1234567890;
    studios BIGINT[] = ARRAY[-1234567891, -1234567890]::BIGINT[];

    returned_brand_id BIGINT;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    DROP TRIGGER refresh_brand_studio_cache_trg ON administration.brand_studios;

    INSERT INTO administration.brand(id) VALUES (brand_id);

    INSERT INTO administration.studio(id)
    SELECT DISTINCT studio_id
    FROM unnest(studios) AS studio_id;


    SELECT administration.add_brand_studios(brand_id, studios) INTO returned_brand_id;

    RETURN NEXT is(
        returned_brand_id,
        brand_id,
        format('Routine "%s" must return provided brand ID', routine_name)
    );
    RETURN NEXT results_eq(
        format('SELECT studio_id FROM administration.brand_studios WHERE brand_id = %s;', brand_id),
        studios,
        format('Routine "%s" must add provided studios', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_add_brand_studios_routine_structure');
select has_function('test_add_brand_studios_routine_behavior');
select finish(true);
