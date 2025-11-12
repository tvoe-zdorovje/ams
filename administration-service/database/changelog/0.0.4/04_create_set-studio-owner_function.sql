CREATE OR REPLACE PROCEDURE administration.set_studio_owner(
    i_studio_id BIGINT,
    i_user_id BIGINT
) AS $$
DECLARE
    _role_id BIGINT;
BEGIN
    SELECT administration.create_standard_role_owner() INTO _role_id;

    INSERT INTO administration.studio_roles(studio_id, role_id)
        VALUES (i_studio_id, _role_id)
            ON CONFLICT DO NOTHING;

    PERFORM administration.add_user_roles(i_user_id, i_studio_id, ARRAY[_role_id]::BIGINT[]);
END;
$$ LANGUAGE plpgsql;
