CREATE OR REPLACE FUNCTION administration.create_brand(
    i_brand_id BIGINT,
    i_user_id BIGINT
) RETURNS BIGINT AS $$
BEGIN
    INSERT INTO brands.brand(id) values (i_brand_id) ON CONFLICT DO NOTHING;
    PERFORM administration.set_brand_owner(i_brand_id, i_user_id);

    RETURN i_brand_id;
END;
$$ LANGUAGE plpgsql;
