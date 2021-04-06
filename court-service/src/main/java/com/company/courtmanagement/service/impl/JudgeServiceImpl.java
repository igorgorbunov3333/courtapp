package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.repository.JudgeRepository;
import com.company.courtmanagement.service.JudgeService;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class JudgeServiceImpl implements JudgeService {
    private JudgeRepository judgeRepository;
    private CourtRepository courtRepository;

    @Autowired
    public JudgeServiceImpl(JudgeRepository judgeRepository, CourtRepository courtRepository) {
        this.judgeRepository = judgeRepository;
        this.courtRepository = courtRepository;
    }

    @Override
    public Collection<Judge> findJudgesByCourtId(long courtId) {
        return null;
    }

    @Override
    public Judge findJudgeById(long judgeId) {
        return judgeRepository.getOne(judgeId);
    }

    @Override
    public Judge saveJudge(Judge judge) {
        if (judge == null) {
            throw new ResourceNotValidException("Judge should not be null");
        }
        judge.validate();
        if (!courtRepository.existsById(judge.getCourt().getCourtId())) {
            throw new ResourceNotValidException(ErrorMsgHelper.COURT_NOT_EXIST);
        }

        return judgeRepository.save(judge);
    }
}
