CREATE OR REPLACE FUNCTION administration.set_role_permissions(
    i_role_id BIGINT,
    i_permissions BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    DELETE FROM administration.role_permissions
    WHERE role_id = i_role_id;

    INSERT INTO administration.role_permissions (role_id, permission_id)
    SELECT DISTINCT i_role_id, permission_id
    FROM unnest(i_permissions) AS permission_id
    WHERE permission_id IS NOT NULL;

    RETURN i_role_id;
END;
$$ LANGUAGE plpgsql;