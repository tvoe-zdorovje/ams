CREATE OR REPLACE FUNCTION administration.create_studio(
    i_studio_id BIGINT,
    i_brand_id BIGINT,
    i_user_id BIGINT
) RETURNS BIGINT AS $$
BEGIN
    INSERT INTO studios.studio(id) values (i_studio_id) ON CONFLICT DO NOTHING;

    INSERT INTO administration.brand_studios(brand_id, studio_id)
        values (create_studio.i_brand_id, i_studio_id)
        ON CONFLICT DO NOTHING;
    -- shouldn't it be one owner for both studio and brand?
    PERFORM administration.set_studio_owner(i_studio_id, i_user_id);

    RETURN i_studio_id;
END;
$$ LANGUAGE plpgsql;
