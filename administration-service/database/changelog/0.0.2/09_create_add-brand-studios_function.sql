CREATE OR REPLACE FUNCTION administration.add_brand_studios(
    i_brand_id BIGINT,
    i_studios BIGINT[]
) RETURNS BIGINT AS $$
BEGIN
    INSERT INTO administration.brand_studios(brand_id, studio_id)
    SELECT DISTINCT i_brand_id, studio_id
    FROM unnest(i_studios) AS studio_id
    WHERE studio_id IS NOT NULL
        ON CONFLICT DO NOTHING;

    RETURN i_brand_id;
END;
$$ LANGUAGE plpgsql;
