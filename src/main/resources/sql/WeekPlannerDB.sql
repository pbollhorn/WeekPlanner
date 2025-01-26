-- Script for creating empty WeekPlannerDB
-- Delete public schema and then recreate it before running script
CREATE TABLE user_data
(
    user_id        SERIAL PRIMARY KEY,
    username       TEXT  NOT NULL UNIQUE,
    password_hash  BYTEA NOT NULL,
    salt           BYTEA NOT NULL,
    encrypted_data BYTEA NOT NULL,
    CONSTRAINT encrypted_data_size CHECK (length(encrypted_data) <= 1048576) -- 1MB constraint
);