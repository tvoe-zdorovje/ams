SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_brands_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        'brands',
        'brand',
        ARRAY [
            'id INT',
            'name VARCHAR(100)',
            'description VARCHAR(255)'
        ],
        'id'
   );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_brands_table_behavior() RETURNS SETOF TEXT AS $$
    DECLARE
        table_name NAME := 'brand';
        table_owner TEXT := _get_rel_owner(table_name);
        brand_id INT := 1;
        name TEXT := 'Lidskoe';
        description TEXT := 'Im loving it';
BEGIN
    EXECUTE format('SET ROLE %I', table_owner);

    PREPARE insert_brand AS INSERT INTO brands."brand" VALUES ($1, $2, $3);
    RETURN NEXT lives_ok(
        format('EXECUTE insert_brand(%L, %L, %L)', brand_id, name, description),
        format('table "%s" must be insertable', UPPER(table_name))
    );
    PREPARE select_brand AS select id from brands."brand" WHERE id=$1;
    RETURN NEXT set_eq(
        format('EXECUTE select_brand(%L)', brand_id),
        ARRAY[brand_id],
        format('table "%s" must be selectable', UPPER(table_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_brands_table_structure');
select has_function('test_brands_table_behavior');
select finish(true);
