BEGIN;

CREATE SCHEMA IF NOT EXISTS tap;
CREATE EXTENSION IF NOT EXISTS pgtap SCHEMA tap;

SET search_path TO tap, users, public;

SELECT plan(1);
SELECT has_table('users', 'Проверка наличия таблицы users');
SELECT finish();

ROLLBACK;
