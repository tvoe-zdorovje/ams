DROP FUNCTION administration.delete_user_roles;

CREATE OR REPLACE FUNCTION administration.delete_user_roles(
    i_user_id BIGINT,
    i_organization_id BIGINT,
    i_roles BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    PERFORM administration.asset_roles_belong_to(i_organization_id, i_roles);

    DELETE FROM administration.user_roles
    WHERE user_id = i_user_id
        AND role_id = ANY(i_roles);

    RETURN i_user_id;
END;
$$ LANGUAGE plpgsql;
