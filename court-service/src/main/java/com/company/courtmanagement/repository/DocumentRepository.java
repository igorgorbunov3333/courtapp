package com.company.courtmanagement.repository;

import com.company.courtmanagement.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
