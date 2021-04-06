package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.repository.CourtCaseRepository;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.service.CourtStageService;
import com.company.courtmanagement.service.exception.ConflictException;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CourtStageServiceImpl implements CourtStageService {
    private CourtStageRepository stageRepository;
    private CourtCaseRepository caseRepository;
    private CourtRepository courtRepository;

    @Autowired
    public CourtStageServiceImpl(CourtStageRepository stageRepository, CourtCaseRepository caseRepository,
                                 CourtRepository courtRepository) {
        this.stageRepository = stageRepository;
        this.caseRepository = caseRepository;
        this.courtRepository = courtRepository;
    }

    @Override
    public CourtStage saveCourtStage(CourtStage courtStage) {
        Collection<CourtStage> previousStages = validateAndFindPreviousStages(courtStage);
        CourtStage previousStage = new ArrayList<>(previousStages).get(previousStages.size() - 1);
        updatePreviousCourtStage(previousStage);
        courtStage.setStart(LocalDate.now());
        courtStage.setCreationDateForDocuments();

        return stageRepository.save(courtStage);
    }

    private Collection<CourtStage> validateAndFindPreviousStages(CourtStage courtStage) {
        if (courtStage == null) {
            throw new ResourceNotValidException("Court stage should not be null");
        }
        courtStage.validate();
        throwExceptionIfNoCourt(courtStage.getCourt().getCourtId());
        throwExceptionIfNoCourtCase(courtStage.getCourtCase().getCourtCaseId());
        Collection<CourtStage> courtStages =
                stageRepository.findByCourtCase_CourtCaseIdOrderByStart(courtStage.getCourtCase().getCourtCaseId());
        if (CollectionUtils.isEmpty(courtStages)) {
            throw new ConflictException("Court case with id " + courtStage.getCourtCase().getCourtCaseId()
                    + " should contain at least one court stage");
        }

        return courtStages;
    }

    private void throwExceptionIfNoCourt(Long courtId) {
        courtRepository.findById(courtId).orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.COURT_NOT_EXIST + courtId));
    }

    private void throwExceptionIfNoCourtCase(Long courtCaseId) {
        caseRepository.findById(courtCaseId).orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.CASE_NOT_EXIST + courtCaseId));
    }

    private void updatePreviousCourtStage(CourtStage previousStage) {
        boolean updatePrevious = false;
        if (previousStage.getStop() == null) {
            previousStage.setStop(LocalDate.now());
            updatePrevious = true;
        }
        if (previousStage.getStageResult() == null) {
            previousStage.setStageResult(CourtStage.StageResult.CLOSED);
            updatePrevious = true;
        }
        if (updatePrevious) {
            stageRepository.save(previousStage);
        }
    }

    @Override
    public Collection<CourtStage> findCourtStages(Collection<Long> courtStageIds) {
        return stageRepository.findAllById(courtStageIds);
    }

    @Override
    public CourtStage findCourtStage(long courtStageId) {
        Optional<CourtStage> optionalStage = stageRepository.findById(courtStageId);
        return optionalStage.orElseThrow(()
                -> new ResourceNotFoundException(ErrorMsgHelper.STAGE_NOT_EXIST + courtStageId));
    }

    @Override
    public void deleteCourtStage(long courtStageId) {
        findCourtStage(courtStageId);
        stageRepository.deleteById(courtStageId);
    }
}
