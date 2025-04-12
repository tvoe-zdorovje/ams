CREATE OR REPLACE FUNCTION administration.set_studio_roles(
    i_studio_id BIGINT,
    i_roles BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    DELETE FROM administration.studio_roles
    WHERE i_studio_id = i_studio_id;

    INSERT INTO administration.studio_roles(studio_id, role_id)
    SELECT DISTINCT i_studio_id, role_id
    FROM unnest(i_roles) AS role_id
    WHERE role_id IS NOT NULL;

    RETURN i_studio_id;
END;
$$ LANGUAGE plpgsql;
