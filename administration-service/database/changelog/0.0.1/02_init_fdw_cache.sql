CREATE TABLE administration.user(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE FUNCTION administration.refresh_fdw_user() RETURNS VOID AS $$
BEGIN
    RAISE LOG 'Refresh FDW cache: user';

    MERGE INTO administration.user AS au
        USING fdw."user" AS fu
        ON au.id = fu.id
        WHEN MATCHED THEN DO NOTHING
        WHEN NOT MATCHED BY SOURCE THEN DELETE
        WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fu.id);
END;
$$ LANGUAGE plpgsql;


CREATE TABLE administration.brand(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE FUNCTION administration.refresh_fdw_brand() RETURNS VOID AS $$
BEGIN
    RAISE LOG 'Refresh FDW cache: brand';

    MERGE INTO administration.brand AS ab
        USING fdw."brand" AS fb
        ON ab.id = fb.id
        WHEN MATCHED THEN DO NOTHING
        WHEN NOT MATCHED BY SOURCE THEN DELETE
        WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fb.id);
END;
$$ LANGUAGE plpgsql;


CREATE TABLE administration.studio(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE FUNCTION administration.refresh_fdw_studio() RETURNS VOID AS $$
BEGIN
    RAISE LOG 'Refresh FDW cache: studio';

    MERGE INTO administration.studio AS "as"
    USING fdw."studio" AS fs
    ON "as".id = fs.id
    WHEN MATCHED THEN DO NOTHING
    WHEN NOT MATCHED BY SOURCE THEN DELETE
    WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fs.id);
END;
$$ LANGUAGE plpgsql;
