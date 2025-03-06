SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_user_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'user',
        columns_with_type := ARRAY[
            'id INT'
        ],
        primary_key := 'id'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_user_table_structure');
select finish(true);
