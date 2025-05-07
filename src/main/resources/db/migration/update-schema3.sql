CREATE SEQUENCE IF NOT EXISTS patients_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE patients
(
    id         BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    birth_date date,
    gender     VARCHAR(255),
    address    VARCHAR(255),
    identifier VARCHAR(255),
    user_id    BIGINT,
    CONSTRAINT pk_patients PRIMARY KEY (id)
);

ALTER TABLE patients
    ADD CONSTRAINT FK_PATIENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);