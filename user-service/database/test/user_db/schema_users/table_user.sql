SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_users_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        'users',
        'user',
        ARRAY [
            'id INT',
            'first_name VARCHAR(50)',
            'last_name VARCHAR(50)'
        ],
        'id'
   );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_users_table_behavior() RETURNS SETOF TEXT AS $$
    DECLARE
        table_name NAME := 'user';
        table_owner TEXT := _get_rel_owner(table_name);
        user_id INT := 1;
        first_name TEXT := 'Mikalay';
        last_name TEXT := 'Charapavitsky';
BEGIN
    EXECUTE format('SET ROLE %I', table_owner);

    PREPARE insert_user AS INSERT INTO users."user" VALUES ($1, $2, $3);
    RETURN NEXT lives_ok(
        format('EXECUTE insert_user(%L, %L, %L)', user_id, first_name, last_name),
        format('table "%s" must be insertable', UPPER(table_name))
    );
    PREPARE select_user AS select id from users."user" WHERE id=$1;
    RETURN NEXT set_eq(
        format('EXECUTE select_user(%L)', user_id),
        ARRAY[user_id],
        format('table "%s" must be selectable', UPPER(table_name))
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_users_table_structure');
select has_function('test_users_table_behavior');
select finish(true);
