SET search_path TO tap, tests, public;


CREATE OR REPLACE FUNCTION tests.test_role_permissions_table_structure() RETURNS SETOF TEXT AS $$
BEGIN
    RETURN QUERY SELECT table_structure(
        schema_name := 'administration',
        table_name := 'role_permissions',
        columns_with_type := ARRAY[
            'role_id BIGINT',
            'permission_id BIGINT'
        ],
        foreign_keys := ARRAY[
            'role_id',
            'permission_id'
        ],
        unique_columns := ARRAY[
            'role_id',
            'permission_id'
        ]
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION tests.test_role_permissions_table_content() RETURNS SETOF TEXT AS $$
BEGIN
    EXECUTE format('SET ROLE %I', _get_schema_owner('administration'));

    CREATE TEMP TABLE tmp_permission AS
        SELECT *
        FROM administration.permission
        WHERE FALSE;

    INSERT INTO tmp_permission(id, name, description) VALUES
    (13300101001,	'GetOrganizationRole',  'Enables read access to a list of organization roles'),
    (13341101002,	'GetUserRoles',         'Enables read access to a list of user roles within an organization'),
    (13341201003,	'AssignRoles',          'Enables access to the role assignment mutation for a user'),
    (13341201004,	'UnassignRoles',        'Enables access to the role unassignment mutation for a user'),
    (13351201005,	'UpdateBrand',          'Enables access to the brand update mutation'),
    (13351201006,	'AssignStudios',        'Enables access to the studio assignment mutation for a brand'),
    (13361201007,	'SaveRole',             'Enables access to the role save mutation'),
    (13361201008,	'CreateBrandRole',      'Enables access to the brand create mutation'),
    (13361201009,	'CreateStudioRole',     'Enables access to the studio create mutation'),
    (13371201010,	'UpdateStudio',         'Enables access to the studio update mutation')
    ;

    RETURN QUERY SELECT results_eq(
        'SELECT id, name, description FROM tmp_permission ORDER BY id',
        'SELECT id, name, description FROM administration.permission ORDER BY id',
        'Table "permission" must contain all required permissions'
    );
END;
$$ LANGUAGE plpgsql;


select plan(2);
select has_function('test_role_permissions_table_structure');
select has_function('test_role_permissions_table_content');
select finish(true);
