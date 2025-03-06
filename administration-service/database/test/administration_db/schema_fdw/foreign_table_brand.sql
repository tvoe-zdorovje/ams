SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_brand_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'fdw',
        table_name := 'brand',
        columns_with_type := ARRAY[
            'id INT'
        ],
        server_name := 'brand_fdw_db',
        schema_name_option := 'brands',
        table_name_option := 'brand'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_brand_table_structure');
select finish(true);
