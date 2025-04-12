CREATE OR REPLACE FUNCTION administration.save_user_roles(
    i_user_id BIGINT,
    i_roles BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    DELETE FROM administration.user_roles
    WHERE user_id = i_user_id
        AND role_id = ANY(i_roles);

    RETURN i_user_id;
END;
$$ LANGUAGE plpgsql;
