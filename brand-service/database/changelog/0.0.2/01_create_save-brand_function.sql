CREATE SEQUENCE IF NOT EXISTS brands.brand_id_seq START WITH 100000000151 INCREMENT 100;


CREATE OR REPLACE FUNCTION brands.next_id(
    seq_name REGCLASS
) RETURNS BIGINT AS $$
DECLARE
    postfix TEXT := '3221100';
BEGIN
    RETURN CAST((nextval(seq_name) || postfix) AS BIGINT);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION brands.save_brand(
    i_id BIGINT,
    i_name VARCHAR(50),
    i_description VARCHAR(50)
) RETURNS BIGINT AS $$
DECLARE
    id_seq_name REGCLASS := 'brands.brand_id_seq';
    o_id BIGINT := i_id;
BEGIN
    IF i_id IS NULL THEN
        INSERT INTO brands."brand"(
            id,
            name,
            description
        ) VALUES (
            brands.next_id(id_seq_name),
            i_name,
            i_description
        )
        RETURNING id INTO o_id;
    ELSE
        UPDATE brands."brand"
            SET
                name = i_name,
                description = i_description
            WHERE id = i_id;
    END IF;

    RETURN o_id;
END;
$$ LANGUAGE plpgsql;
