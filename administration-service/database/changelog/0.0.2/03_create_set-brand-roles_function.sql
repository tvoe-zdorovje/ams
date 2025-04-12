CREATE OR REPLACE FUNCTION administration.set_brand_roles(
    i_brand_id BIGINT,
    i_roles BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    DELETE FROM administration.brand_roles
    WHERE i_brand_id = i_brand_id;

    INSERT INTO administration.brand_roles(brand_id, role_id)
    SELECT DISTINCT i_brand_id, role_id
    FROM unnest(i_roles) AS role_id
    WHERE role_id IS NOT NULL;

    RETURN i_brand_id;
END;
$$ LANGUAGE plpgsql;
