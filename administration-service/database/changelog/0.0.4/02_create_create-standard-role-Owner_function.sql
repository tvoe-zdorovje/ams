CREATE OR REPLACE FUNCTION administration.create_standard_role_owner() RETURNS BIGINT AS $$
DECLARE
    _schema_name    TEXT = 'administration';
    _schema_owner   TEXT;

    _role_id        BIGINT = nextval('administration.role_id_seq');
    _role_name      VARCHAR(50) = 'Owner';
    _role_desc      TEXT = 'Owner role grants all available permissions for the entity';
    _permissions    BIGINT[];
BEGIN
    SELECT schema_owner
    FROM information_schema.schemata
    WHERE schema_name = _schema_name
    INTO _schema_owner;

    INSERT INTO administration.role(id, name, description, is_standard) VALUES
        (_role_id, _role_name, _role_desc, true);

    SELECT ARRAY(SELECT id FROM administration.permission) INTO _permissions;
    PERFORM administration.set_role_permissions(_role_id, _permissions);

    RETURN _role_id;
END;
$$ LANGUAGE plpgsql;
