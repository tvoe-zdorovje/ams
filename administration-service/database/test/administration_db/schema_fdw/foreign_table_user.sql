SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_user_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT foreign_table_structure(
        schema_name := 'fdw',
        table_name := 'user',
        columns_with_type := ARRAY[
            'id INT'
        ],
        server_name := 'user_fdw_db',
        schema_name_option := 'users',
        table_name_option := 'user'
    );
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_user_table_structure');
select finish(true);
