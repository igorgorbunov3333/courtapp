package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Judge;

import java.util.Collection;

public interface JudgeService {

    Collection<Judge> findJudgesByCourtId(long courtId);

    Judge findJudgeById(long judgeId);

    Judge saveJudge(Judge judge);
}
