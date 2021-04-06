package com.company.courtmanagement.service;

import com.company.courtmanagement.model.CourtStage;

import java.util.Collection;

public interface CourtStageService {
    CourtStage saveCourtStage(CourtStage courtStage);

    Collection<CourtStage> findCourtStages(Collection<Long> courtStageIds);

    CourtStage findCourtStage(long courtStageId);

    void deleteCourtStage(long courtStageId);
}
