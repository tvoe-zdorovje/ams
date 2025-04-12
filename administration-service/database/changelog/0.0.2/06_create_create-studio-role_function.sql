CREATE OR REPLACE FUNCTION administration.create_studio_role(
    i_studio_id BIGINT,
    i_name VARCHAR(50),
    i_description VARCHAR(50),
    i_permissions BIGINT[]
) RETURNS BIGINT AS $$
DECLARE
    _role_id BIGINT;
BEGIN
    _role_id = administration.save_role(
        null,
        i_name,
        i_description,
        i_permissions
               );

    INSERT INTO administration.studio_roles(studio_id, role_id) VALUES (i_studio_id, _role_id);

    RETURN _role_id;
END;
$$ LANGUAGE plpgsql;
