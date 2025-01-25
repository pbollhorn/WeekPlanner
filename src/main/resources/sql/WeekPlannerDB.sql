DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
CREATE TABLE public.user_data
(
    user_id        SERIAL PRIMARY KEY,
    username       TEXT UNIQUE,
    password_hash  BYTEA,
    salt           BYTEA,
    encrypted_data BYTEA
);

DROP SCHEMA IF EXISTS test CASCADE;
CREATE SCHEMA test;
CREATE TABLE test.user_data
(
    LIKE public.user_data INCLUDING CONSTRAINTS
);