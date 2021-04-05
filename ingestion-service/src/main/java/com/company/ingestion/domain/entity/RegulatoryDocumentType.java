package com.company.ingestion.domain.entity;

import lombok.AllArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
public class RegulatoryDocumentType {

    @Id
    private Long id;

    @Column(nullable = false)
    private String type;
}
