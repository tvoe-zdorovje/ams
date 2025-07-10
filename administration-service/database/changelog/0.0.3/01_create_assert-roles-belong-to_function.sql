CREATE OR REPLACE FUNCTION administration.asset_roles_belong_to(
    i_organization_id BIGINT,
    i_roles BIGINT[]
) RETURNS VOID AS $$
DECLARE
    count INT;
BEGIN
    SELECT count(role_id) INTO count
    FROM (
        SELECT administration.studio_roles.role_id
        FROM administration.studio_roles
        WHERE administration.studio_roles.studio_id = i_organization_id

        UNION

        SELECT administration.brand_roles.role_id
        FROM administration.brand_roles
        WHERE administration.brand_roles.brand_id = i_organization_id
    ) AS roles
    WHERE roles.role_id = ANY(i_roles);

    IF count != array_length(i_roles, 1) THEN
        RAISE EXCEPTION 'Some of the roles do not belong to the organization';
    END IF;
END;
$$ LANGUAGE plpgsql;
