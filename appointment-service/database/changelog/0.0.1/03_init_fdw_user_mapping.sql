-- TODO sensitive data
CREATE USER MAPPING FOR apsportal SERVER user_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');

CREATE USER MAPPING FOR apsportal SERVER brand_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');

CREATE USER MAPPING FOR apsportal SERVER studio_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');
