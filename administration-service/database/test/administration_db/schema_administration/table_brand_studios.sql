SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_brand_studios_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'brand_studios',
        columns_with_type := ARRAY[
            'brand_id INT',
            'studio_id INT'
        ],
        foreign_keys := ARRAY[
            'brand_id',
            'studio_id'
        ],
        unique_columns := null
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_brand_studios_table_structure');
select finish(true);
