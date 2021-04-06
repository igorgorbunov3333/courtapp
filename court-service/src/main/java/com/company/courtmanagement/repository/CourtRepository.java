package com.company.courtmanagement.repository;

import com.company.courtmanagement.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
