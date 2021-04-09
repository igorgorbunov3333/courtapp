--liquibase formatted sql

--changeset ihorbunov:1
alter table regulatory_document_type add column name text;
update regulatory_document_type set name = type;
alter table regulatory_document_type alter column name set not null;