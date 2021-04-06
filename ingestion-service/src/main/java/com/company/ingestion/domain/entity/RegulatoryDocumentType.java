package com.company.ingestion.domain.entity;

import lombok.AllArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@AllArgsConstructor
public class RegulatoryDocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regulatory_document_type_seq")
    @SequenceGenerator(name = "regulatory_document_type_seq", sequenceName = "regulatory_document_type_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String type;
}
