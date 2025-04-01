SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_save_brand_routine_structure() RETURNS SETOF TEXT AS $$
declare
    schema_name TEXT := 'brands';
    routine_name TEXT := 'save_brand';
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

CREATE OR REPLACE FUNCTION tests.test_save_brand_routine_behavior() RETURNS SETOF TEXT AS $$
DECLARE
    routine_name TEXT := 'brands.save_brand';

    actual_generated_id BIGINT;
    expected_generated_id BIGINT;

    initial_name VARCHAR(50) := 'initial_name';
    initial_description VARCHAR(50) := 'initial_description';

    updated_name VARCHAR(50) := 'updated_name';
    updated_description VARCHAR(50) := 'updated_description';

    isBrandUpdated BOOLEAN;
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('brands'));

    SELECT brands.save_brand(null, initial_name, initial_description) INTO actual_generated_id;
    SELECT brands.next_id('brands.brand_id_seq') - 1000000000 INTO expected_generated_id;


    RETURN NEXT is(
        actual_generated_id,
        expected_generated_id,
        format('Routine "%s" must create a new brand with a correctly generated ID', routine_name)
    );


    PERFORM brands.save_brand(actual_generated_id, updated_name, updated_description);


    SELECT count(*)::INT::BOOLEAN INTO isBrandUpdated FROM brands."brand" WHERE
        id = actual_generated_id AND
        name = updated_name AND
        description = updated_description;

    RETURN NEXT ok(
        isBrandUpdated,
        format('Routine "%s" must update the brand correctly', routine_name)
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_save_brand_routine_structure');
select has_function('test_save_brand_routine_behavior');
select finish(true);
