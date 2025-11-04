CREATE OR REPLACE FUNCTION administration.create_role_owner() RETURNS VOID AS $$
DECLARE
    _schema_name    TEXT = 'administration';
    _schema_owner   TEXT;

    _role_id        BIGINT = 1999999999613321100;
    _permissions    BIGINT[];
BEGIN
    SELECT schema_owner
    FROM information_schema.schemata
    WHERE schema_name = _schema_name
    INTO _schema_owner;
    EXECUTE format('SET ROLE %I', _schema_owner);

    INSERT INTO administration.role(id, name, description) VALUES
        (_role_id, 'Owner', 'Owner role grants all available permissions for the entity');

    SELECT ARRAY(SELECT id FROM administration.permission) INTO _permissions;
    PERFORM administration.set_role_permissions(_role_id, _permissions);

    RESET ROLE;
END;
$$ LANGUAGE plpgsql;

SELECT administration.create_role_owner();

DROP FUNCTION administration.create_role_owner();
