

CREATE SEQUENCE IF NOT EXISTS test_seq START WITH 1 INCREMENT BY 1 MINVALUE 1;

CREATE TABLE IF NOT EXISTS test (
    id           BIGINT PRIMARY KEY DEFAULT nextval('test_seq'),
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now()
);

