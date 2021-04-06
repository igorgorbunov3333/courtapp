package com.company.courtmanagement.service;

import com.company.courtmanagement.model.CourtHearing;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtHearingRepository;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.CourtHearingServiceImp;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CourtHearingServiceTest {
    private CourtHearingRepository hearingRepository = mock(CourtHearingRepository.class);
    private CourtStageRepository stageRepository = mock(CourtStageRepository.class);
    private CourtHearingService hearingService =
            new CourtHearingServiceImp(hearingRepository, stageRepository);

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtHearing_whenNull_thenFail() {
        hearingService.saveCourtHearing(null);
        verifyZeroInteractions(hearingRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtHearing_whenNoCourtStage_thenFail() {
        CourtHearing courtHearing = new CourtHearing();
        hearingService.saveCourtHearing(courtHearing);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtHearing_whenNoCourtStage_Id_thenFail() {
        CourtHearing courtHearing = new CourtHearing();
        courtHearing.setCourtStage(new CourtStage());
        hearingService.saveCourtHearing(courtHearing);
    }

    @Test
    public void testSaveCourtHearing_whenValid_thenAddDateOfHearing() {
        CourtHearing courtHearing = spy(CourtHearing.class);
        courtHearing.setCourtStage(new CourtStage(1L));

        when(stageRepository.findById(1L)).thenReturn(Optional.of(new CourtStage()));
        hearingService.saveCourtHearing(courtHearing);
        assertNotNull(courtHearing.getCourtHearingDate());
    }

    @Test
    public void testSaveCourtHearing_whenSaveWithDocuments_thenAddCreationDatesForDocuments() {
        CourtHearing courtHearing = spy(CourtHearing.class);
        courtHearing.setCourtStage(new CourtStage(1L));

        Document document = new Document();
        Collection<Document> documents = Collections.singleton(document);
        courtHearing.setDocuments(documents);

        when(stageRepository.findById(1L)).thenReturn(Optional.of(new CourtStage()));
        hearingService.saveCourtHearing(courtHearing);
        assertNotNull(courtHearing.getDocuments().iterator().next().getCreationDate());
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtHearing_whenCourtStageNotExists_thenFail() {
        CourtHearing courtHearing = spy(CourtHearing.class);
        courtHearing.setCourtStage(new CourtStage(1L));

        when(stageRepository.findById(1L)).thenReturn(Optional.empty());
        hearingService.saveCourtHearing(courtHearing);
    }

    @Test
    public void testSaveCourtHearing_whenValid_thenSaveSuccessfully() {
        CourtHearing courtHearing = spy(CourtHearing.class);
        courtHearing.setCourtStage(new CourtStage(1L));

        when(stageRepository.findById(1L)).thenReturn(Optional.of(new CourtStage()));
        hearingService.saveCourtHearing(courtHearing);
        verify(hearingRepository).save(courtHearing);
    }

}
