CREATE TABLE administration.user(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE PROCEDURE administration.refresh_fdw_user()
    LANGUAGE SQL
BEGIN ATOMIC
    MERGE INTO administration.user AS au
        USING fdw."user" AS fu
        ON au.id = fu.id
        WHEN MATCHED THEN DO NOTHING
        WHEN NOT MATCHED BY SOURCE THEN DELETE
        WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fu.id);
END;


CREATE TABLE administration.brand(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE PROCEDURE administration.refresh_fdw_brand()
    LANGUAGE SQL
BEGIN ATOMIC
    MERGE INTO administration.brand AS ab
        USING fdw."brand" AS fb
        ON ab.id = fb.id
        WHEN MATCHED THEN DO NOTHING
        WHEN NOT MATCHED BY SOURCE THEN DELETE
        WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fb.id);
END;


CREATE TABLE administration.studio(
    id BIGINT PRIMARY KEY
);

CREATE OR REPLACE PROCEDURE administration.refresh_fdw_studio()
    LANGUAGE SQL
BEGIN ATOMIC
    MERGE INTO administration.studio AS "as"
    USING fdw."studio" AS fs
    ON "as".id = fs.id
    WHEN MATCHED THEN DO NOTHING
    WHEN NOT MATCHED BY SOURCE THEN DELETE
    WHEN NOT MATCHED BY TARGET THEN INSERT (id) VALUES (fs.id);
END;
