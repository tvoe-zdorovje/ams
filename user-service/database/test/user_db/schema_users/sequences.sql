SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_user_id_seq_sequence_must_exist() RETURNS SETOF TEXT AS $$
DECLARE
    schema_name NAME := 'users';
    sequences NAME[] := ARRAY['user_id_seq'];
BEGIN
    RETURN NEXT sequences_are(schema_name, sequences);
END;
$$ LANGUAGE plpgsql;


select plan(1);
select has_function('test_user_id_seq_sequence_must_exist');
select finish(true);
