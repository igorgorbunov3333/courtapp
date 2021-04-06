CREATE SEQUENCE IF NOT EXISTS courts_seq;

CREATE TABLE courts(id BIGINT PRIMARY KEY NOT NULL,  name VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL, building VARCHAR(255) NOT NULL, region VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL, proceeding VARCHAR(255) NOT NULL);

CREATE SEQUENCE IF NOT EXISTS court_cases_seq;

CREATE TABLE court_cases(id BIGINT PRIMARY KEY NOT NULL, account_id BIGINT NOT NULL);

CREATE SEQUENCE IF NOT EXISTS judges_seq;

CREATE TABLE judges(id BIGINT PRIMARY KEY NOT NULL, first_name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL, patronymic VARCHAR(255) NOT NULL, court_id INT,
    FOREIGN KEY(court_id) REFERENCES courts(id));

CREATE SEQUENCE IF NOT EXISTS court_stages_seq;

CREATE TABLE court_stages(id BIGINT PRIMARY KEY NOT NULL, stage_result VARCHAR(255), court_id BIGINT NOT NULL,
    start DATE NOT NULL, stop DATE, court_case_id BIGINT NOT NULL, FOREIGN KEY(court_id) REFERENCES courts(id),
    FOREIGN KEY(court_case_id) REFERENCES court_cases(id));

CREATE SEQUENCE IF NOT EXISTS court_hearings_seq;

CREATE TABLE court_hearings(id BIGINT PRIMARY KEY NOT NULL, court_stage_id BIGINT NOT NULL,
    court_hearing_result VARCHAR(255), court_hearing_date DATE, FOREIGN KEY(court_stage_id)
    REFERENCES court_stages(id));

CREATE SEQUENCE IF NOT EXISTS documents_seq;

CREATE TABLE documents(id BIGINT PRIMARY KEY NOT NULL, creation_date DATE NOT NULL, court_stage_id BIGINT,
    court_hearing_id BIGINT, FOREIGN KEY(court_stage_id) REFERENCES court_stages(id), FOREIGN KEY(court_hearing_id)
    REFERENCES court_hearings(id));
