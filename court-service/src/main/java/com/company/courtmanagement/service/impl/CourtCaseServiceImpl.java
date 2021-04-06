package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.repository.CourtCaseRepository;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.service.CourtCaseService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;

@Service
public class CourtCaseServiceImpl implements CourtCaseService {
    private CourtCaseRepository caseRepository;
    private CourtRepository courtRepository;

    @Autowired
    public CourtCaseServiceImpl(CourtCaseRepository caseRepository, CourtRepository courtRepository) {
        this.caseRepository = caseRepository;
        this.courtRepository = courtRepository;
    }

    @Override
    public CourtCase saveCourtCase(CourtCase courtCase) {
        validate(courtCase);

        CourtStage courtStage = courtCase.getCourtStages().iterator().next();
        courtStage.setStart(LocalDate.now());
        courtStage.setCreationDateForDocuments();
        return caseRepository.save(courtCase);
    }

    private void validate(CourtCase courtCase) {
        if (courtCase == null) {
            throw new ResourceNotValidException("Court case should not be null");
        }

        courtCase.getOptionalAccountId().orElseThrow(()
                -> new ResourceNotValidException("Court case should contain account id"));

        if (CollectionUtils.isEmpty(courtCase.getCourtStages())) {
            throw new ResourceNotValidException("Court case should contain court stage");
        }
        if (courtCase.getCourtStages().size() > 1) {
            throw new ResourceNotValidException("Court case should not contain more then one court stage during save");
        }

        CourtStage courtStage = courtCase.getCourtStages().iterator().next();
        if (CollectionUtils.isEmpty(courtStage.getDocuments())) {
            throw new ResourceNotValidException(ErrorMsgHelper.STAGE_NO_DOC);
        }
        Long courtId = courtStage.getOptionalCourt().flatMap(Court::getOptionalId).orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.STAGE_NO_COURT));

        courtRepository.findById(courtId).orElseThrow(()
                -> new ResourceNotValidException("No court with id " + courtId));
    }

    @Override
    public CourtCase findCourtCase(long courtCaseId) {
        return caseRepository.findById(courtCaseId).orElseThrow(()
                -> new ResourceNotFoundException("Court case with such id not found: " + courtCaseId));
    }

    @Override
    public void deleteCourtCase(long courtCaseId) {
        findCourtCase(courtCaseId);
        caseRepository.deleteById(courtCaseId);
    }
}
