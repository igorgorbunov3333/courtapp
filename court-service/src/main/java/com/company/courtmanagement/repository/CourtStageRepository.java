package com.company.courtmanagement.repository;

import com.company.courtmanagement.model.CourtStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

@SuppressWarnings("PMD.MethodNamingConventions")
public interface CourtStageRepository extends JpaRepository<CourtStage, Long> {
    Collection<CourtStage> findByCourtCase_CourtCaseIdOrderByStart(Long courtCaseId);
}
