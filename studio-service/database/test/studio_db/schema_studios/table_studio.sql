SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_studios_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        'studios',
        'studio',
        ARRAY [
            'id BIGINT',
            'name VARCHAR(100)',
            'description VARCHAR(255)'
        ],
        'id'
   );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_studios_table_behavior() RETURNS SETOF TEXT AS $$
    DECLARE
        table_name NAME := 'studio';
        table_owner TEXT := _get_rel_owner(table_name);
        studio_id BIGINT := 1;
        name TEXT := 'Mini-Mash Nail Studio';
        description TEXT := 'From Salihorsk onto your hands';
BEGIN
    EXECUTE format('SET ROLE %I', table_owner);

    PREPARE insert_studio AS INSERT INTO studios."studio" VALUES ($1, $2, $3);
    RETURN NEXT lives_ok(
        format('EXECUTE insert_studio(%L, %L, %L)', studio_id, name, description),
        format('table "%s" must be insertable', UPPER(table_name))
    );
    PREPARE select_studio AS select id from studios."studio" WHERE id=$1;
    RETURN NEXT set_eq(
        format('EXECUTE select_studio(%L)', studio_id),
        ARRAY[studio_id],
        format('table "%s" must be selectable', UPPER(table_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_studios_table_structure');
select has_function('test_studios_table_behavior');
select finish(true);
