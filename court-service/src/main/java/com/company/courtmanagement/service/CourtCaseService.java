package com.company.courtmanagement.service;

import com.company.courtmanagement.model.CourtCase;

public interface CourtCaseService {
    CourtCase saveCourtCase(CourtCase courtCase);

    CourtCase findCourtCase(long courtCaseId);

    void deleteCourtCase(long courtCaseId);
}
