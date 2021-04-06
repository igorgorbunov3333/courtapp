package com.company.courtmanagement.repository;

import com.company.courtmanagement.model.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeRepository extends JpaRepository<Judge, Long> {
}
