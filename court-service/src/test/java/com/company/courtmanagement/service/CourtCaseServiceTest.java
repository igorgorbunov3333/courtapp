package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtCaseRepository;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.CourtCaseServiceImpl;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CourtCaseServiceTest {
    private CourtRepository courtRepository = mock(CourtRepository.class);
    private CourtCaseRepository caseRepository = mock(CourtCaseRepository.class);
    private CourtCaseService caseService =
            new CourtCaseServiceImpl(caseRepository, courtRepository);

    @Test(expected = ResourceNotFoundException.class)
    public void testFindCourtCase_whenNotExists_thenFail() {
        when(caseRepository.findById(1L)).thenReturn(Optional.empty());
        caseService.findCourtCase(1L);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenNull_thenFail() {
        caseService.saveCourtCase(null);
        verifyZeroInteractions(caseRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenNoCourtStage_thenFail() {
        CourtCase courtCase = new CourtCase();
        caseService.saveCourtCase(courtCase);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenNoCourtStage_Court_thenFail() {
        CourtCase courtCase = new CourtCase();
        courtCase.addCourtStage(new CourtStage());
        caseService.saveCourtCase(courtCase);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenNoCourtStage_Document_thenFail() {
        CourtCase courtCase = buildCourtCase();
        courtCase.getCourtStages().iterator().next().setDocuments(Collections.emptyList());
        caseService.saveCourtCase(courtCase);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenCourtNotExists_thenFail() {
        CourtCase courtCase = buildCourtCase();
        when(courtRepository.findById(1L)).thenReturn(Optional.empty());
        caseService.saveCourtCase(courtCase);
        verify(courtRepository).findById(1L);
    }

    @Test
    public void testSaveCourtCase_whenValid_thenSuccessfullySave() {
        CourtCase courtCase = buildCourtCase();
        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));
        caseService.saveCourtCase(courtCase);
        verify(courtRepository).findById(1L);
    }

    @Test
    public void testSaveCourtCase_whenValid_thenSetStartDateForCourtStage() {
        CourtCase courtCase = new CourtCase();
        CourtStage courtStage = spy(CourtStage.class);
        courtStage.setCourt(new Court(1L));
        courtStage.addDocument(new Document());
        courtCase.addCourtStage(courtStage);
        courtCase.setAccountId(1L);

        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));
        caseService.saveCourtCase(courtCase);
        assertNotNull(courtStage.getStart());
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenNoAccountId_thenFail() {
        CourtCase courtCase = buildCourtCase();
        courtCase.setAccountId(null);

        caseService.saveCourtCase(courtCase);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtCase_whenSaveWithMoreThanOneCourtStage_thenFail() {
        CourtCase courtCase = buildCourtCase();

        CourtStage courtStage2 = buildCourtStage();
        courtCase.addCourtStage(courtStage2);

        courtCase.setAccountId(1L);
        when(courtRepository.getOne(1L)).thenReturn(new Court());
        caseService.saveCourtCase(courtCase);
    }

    @Test
    public void testSaveCourtCase_whenValid_thenSetCreationDateForAllDocuments() {
        CourtCase courtCase = new CourtCase();
        CourtStage courtStage = spy(CourtStage.class);
        courtStage.setCourt(new Court(1L));
        Document document = new Document();
        courtStage.addDocument(document);
        courtCase.addCourtStage(courtStage);
        courtCase.setAccountId(1L);

        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));
        caseService.saveCourtCase(courtCase);
        assertNotNull(courtStage.getDocuments().iterator().next().getCreationDate());
    }

    private CourtCase buildCourtCase() {
        CourtCase courtCase = new CourtCase();
        CourtStage courtStage = buildCourtStage();
        courtCase.addCourtStage(courtStage);
        courtCase.setAccountId(1L);
        return courtCase;
    }

    private CourtStage buildCourtStage() {
        CourtStage courtStage = new CourtStage();
        courtStage.setCourt(new Court(1L));
        courtStage.addDocument(new Document());
        return courtStage;
    }
}
