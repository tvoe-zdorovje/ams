CREATE OR REPLACE FUNCTION administration.add_user_roles(
    i_user_id BIGINT,
    i_roles BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    INSERT INTO administration.user_roles (user_id, role_id)
    SELECT DISTINCT i_user_id, role_id
    FROM unnest(i_roles) AS role_id
    WHERE role_id IS NOT NULL
    ON CONFLICT DO NOTHING;

    RETURN i_user_id;
END;
$$ LANGUAGE plpgsql;
