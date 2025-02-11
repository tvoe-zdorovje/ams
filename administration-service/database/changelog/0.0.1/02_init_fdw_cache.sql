CREATE TABLE administration.user AS
    SELECT * FROM fdw."user"
    WITH NO DATA;

ALTER TABLE administration.user ADD PRIMARY KEY (id);

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


CREATE TABLE administration.brand AS
    SELECT * FROM fdw.brand
    WITH NO DATA;

ALTER TABLE administration.brand ADD PRIMARY KEY (id);

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


CREATE TABLE administration.studio AS
    SELECT * FROM fdw.studio
    WITH NO DATA;

ALTER TABLE administration.studio ADD PRIMARY KEY (id);

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
