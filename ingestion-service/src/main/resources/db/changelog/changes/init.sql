--liquibase formatted sql

--changeset ihorbunov:1
create table regulatory_document_type (
    id bigint primary key not null,
    type text not null
);

CREATE SEQUENCE IF NOT EXISTS regulatory_document_type_seq;
