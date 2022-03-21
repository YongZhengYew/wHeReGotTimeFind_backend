CREATE SCHEMA IF NOT EXISTS test;

CREATE TABLE IF NOT EXISTS test.messages (
    msg_id serial primary key,
    created_time timestamp NOT NULL,
    msg text NOT NULL
);