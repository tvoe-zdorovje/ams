-- TODO sensitive data
CREATE USER MAPPING FOR adsportal SERVER user_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');

CREATE USER MAPPING FOR adsportal SERVER brand_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');

CREATE USER MAPPING FOR adsportal SERVER studio_fdw_db
    OPTIONS (user 'fdw_user', password 'fdw_user');
