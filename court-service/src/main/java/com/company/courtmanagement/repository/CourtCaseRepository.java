package com.company.courtmanagement.repository;

import com.company.courtmanagement.model.CourtCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtCaseRepository extends JpaRepository<CourtCase, Long> {
}
