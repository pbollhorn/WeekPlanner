-- Create public schema
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
CREATE TABLE public.user_data
(
    user_id        SERIAL PRIMARY KEY,
    username       TEXT UNIQUE NOT NULL,
    password_hash  BYTEA NOT NULL,
    salt           BYTEA NOT NULL,
    encrypted_data BYTEA NOT NULL
);

-- Create test schema
DROP SCHEMA IF EXISTS test CASCADE;
CREATE SCHEMA test;
CREATE TABLE test.user_data
(
    LIKE public.user_data INCLUDING DEFAULTS INCLUDING INDEXES INCLUDING CONSTRAINTS
);