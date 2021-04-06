package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.CourtHearing;
import com.company.courtmanagement.repository.CourtHearingRepository;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.service.CourtHearingService;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CourtHearingServiceImp implements CourtHearingService {
    private CourtHearingRepository hearingRepository;
    private CourtStageRepository stageRepository;

    @Autowired
    public CourtHearingServiceImp(CourtHearingRepository hearingRepository,
                                  CourtStageRepository stageRepository) {
        this.hearingRepository = hearingRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public CourtHearing saveCourtHearing(CourtHearing courtHearing) {
        if (courtHearing == null) {
            throw new ResourceNotValidException("Court hearing should not be null");
        }
        courtHearing.validate();
        long courtStageId = courtHearing.getCourtStage().getCourtStageId();
        stageRepository.findById(courtStageId).orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.STAGE_NOT_EXIST + courtStageId));
        courtHearing.setCourtHearingDate(LocalDate.now());
        courtHearing.getDocuments().forEach(d -> d.setCreationDate(LocalDate.now()));
        return hearingRepository.save(courtHearing);
    }
}
