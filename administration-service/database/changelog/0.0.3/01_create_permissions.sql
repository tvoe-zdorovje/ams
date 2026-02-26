CREATE OR REPLACE FUNCTION administration.create_permissions() RETURNS VOID AS $$
DECLARE
    _schema_name  TEXT = 'administration';
    _schema_owner TEXT;
BEGIN
    SELECT schema_owner
    FROM information_schema.schemata
    WHERE schema_name = _schema_name
    INTO _schema_owner;
    EXECUTE format('SET ROLE %I', _schema_owner);

    INSERT INTO administration.permission(id, name, description) VALUES
        (13300101001,	'GetOrganizationRole',  'Enables read access to a list of organization roles'),
        (13341101002,	'GetUserRoles',         'Enables read access to a list of user roles within an organization'),
        (13341201003,	'AssignRoles',          'Enables access to the role assignment mutation for a user'),
        (13341201004,	'UnassignRoles',        'Enables access to the role unassignment mutation for a user'),
        (13351201005,	'UpdateBrand',          'Enables access to the brand update mutation'),
        (13351201006,	'AssignStudios',        'Enables access to the studio assignment mutation for a brand'),
        (13361201007,	'SaveRole',             'Enables access to the role save mutation'),
        (13361201008,	'CreateBrandRole',      'Enables access to the brand create mutation'),
        (13361201009,	'CreateStudioRole',     'Enables access to the studio create mutation'),
        (13371201010,	'UpdateStudio',         'Enables access to the studio update mutation');

    RESET ROLE;
END;
$$ LANGUAGE plpgsql;

SELECT administration.create_permissions();

DROP FUNCTION administration.create_permissions();
