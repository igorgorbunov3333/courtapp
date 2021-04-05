package com.company.ingestion.repository;

import com.company.ingestion.domain.entity.RegulatoryDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulatoryDocumentTypeRepository extends JpaRepository<RegulatoryDocumentType, Long> {
}
