CREATE OR REPLACE FUNCTION brands.update_brand(
    i_id BIGINT,
    i_name VARCHAR(50),
    i_description VARCHAR(50)
) RETURNS BIGINT AS $$
BEGIN
    UPDATE brands."brand"
    SET
        name = i_name,
        description = i_description
    WHERE id = i_id;

    RETURN i_id;
END;
$$ LANGUAGE plpgsql;
