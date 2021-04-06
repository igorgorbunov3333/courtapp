package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtCaseRepository;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.service.exception.ConflictException;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.CourtStageServiceImpl;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CourtStageServiceTest {
    private CourtStageRepository stageRepository = mock(CourtStageRepository.class);
    private CourtCaseRepository caseRepository = mock(CourtCaseRepository.class);
    private CourtRepository courtRepository = mock(CourtRepository.class);
    private CourtStageService stageService =
            new CourtStageServiceImpl(stageRepository, caseRepository, courtRepository);

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtStage_whenNull_thenFail() {
        stageService.saveCourtStage(null);
        verifyZeroInteractions(stageRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtStage_whenNoDocument_thenFail() {
        CourtStage courtStage = buildCourtStage();
        courtStage.setDocuments(Collections.emptyList());
        stageService.saveCourtStage(courtStage);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourtStage_whenNoCourt_thenFail() {
        CourtStage courtStage = buildCourtStage();
        courtStage.setCourt(null);
        stageService.saveCourtStage(courtStage);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testCourtStageSave_whenNoCourtCase_thenFail() {
        CourtStage courtStage = buildCourtStage();
        courtStage.setCourtCase(null);
        stageService.saveCourtStage(courtStage);
    }

    @Test(expected = ConflictException.class)
    public void testCourtStageSave_whenPreviousCourtStageNotExists_thenFail() {
        CourtStage courtStage = buildCourtStage();
        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));
        when(caseRepository.findById(1L)).thenReturn(Optional.of(new CourtCase()));
        when(stageRepository.findByCourtCase_CourtCaseIdOrderByStart(1L)).thenReturn(Collections.emptyList());
        stageService.saveCourtStage(courtStage);
    }

    @Test
    public void testSave_whenSaveNewStage_thenNeedToUpdatePreviousWithStatusClosedAndWithStopDateAndNewWithStart() {
        CourtStage courtStage = buildCourtStage();

        CourtStage firstCourtStage = new CourtStage();
        firstCourtStage.setCourtStageId(1L);
        firstCourtStage.setCourtCase(new CourtCase(1L));
        firstCourtStage.setDocuments(Collections.singletonList(new Document(1L)));
        firstCourtStage.setCourt(new Court(1L));
        firstCourtStage.setStart(LocalDate.of(2020, 3, 1));
        firstCourtStage.setStop(LocalDate.of(2020, 3, 28));

        CourtStage previousCourtStage = spy(CourtStage.class);
        previousCourtStage.setCourtStageId(2L);
        previousCourtStage.setCourtCase(new CourtCase(1L));
        previousCourtStage.setDocuments(Collections.singletonList(new Document(2L)));
        previousCourtStage.setCourt(new Court(1L));
        previousCourtStage.setStart(LocalDate.of(2020, 4, 1));
        Collection<CourtStage> courtStages = List.of(firstCourtStage, previousCourtStage);

        when(stageRepository.findByCourtCase_CourtCaseIdOrderByStart(1L)).thenReturn(courtStages);
        when(stageRepository.save(courtStage)).thenReturn(courtStage);
        when(stageRepository.save(previousCourtStage)).thenReturn(previousCourtStage);
        when(caseRepository.findById(1L)).thenReturn(Optional.of(new CourtCase()));
        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));

        stageService.saveCourtStage(courtStage);
        assertNotNull(previousCourtStage.getStageResult());
        assertEquals(previousCourtStage.getStop(), LocalDate.now());
        assertEquals(courtStage.getStart(), LocalDate.now());
    }

    @Test(expected = ResourceNotValidException.class)
    public void testCourtStageSave_whenCourtNotExists_thenFail() {
        CourtStage courtStage = buildCourtStage();

        when(caseRepository.findById(1L)).thenReturn(Optional.of(new CourtCase()));
        when(courtRepository.findById(1L)).thenReturn(Optional.empty());
        stageService.saveCourtStage(courtStage);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testCourtStageSave_whenCourtCaseNotExists_thenFail() {
        CourtStage courtStage = buildCourtStage();

        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));
        when(caseRepository.findById(1L)).thenReturn(Optional.empty());
        stageService.saveCourtStage(courtStage);
    }

    @Test
    public void testCourtStageSave_whenValid_thenSuccessfullySave() {
        CourtStage courtStage = buildCourtStage();

        CourtStage firstCourtStage = new CourtStage();
        firstCourtStage.setCourtStageId(1L);
        firstCourtStage.setCourtCase(new CourtCase(1L));
        firstCourtStage.setDocuments(Collections.singletonList(new Document(1L)));
        firstCourtStage.setCourt(new Court(1L));
        firstCourtStage.setStart(LocalDate.of(2020, 3, 1));
        firstCourtStage.setStop(LocalDate.of(2020, 3, 28));

        CourtStage secondCourtStage = new CourtStage();
        secondCourtStage.setCourtStageId(2L);
        secondCourtStage.setCourtCase(new CourtCase(1L));
        secondCourtStage.setDocuments(Collections.singletonList(new Document(2L)));
        secondCourtStage.setCourt(new Court(1L));
        secondCourtStage.setStart(LocalDate.of(2020, 4, 1));
        Collection<CourtStage> courtStages = List.of(firstCourtStage, secondCourtStage);

        when(stageRepository.findByCourtCase_CourtCaseIdOrderByStart(1L)).thenReturn(courtStages);
        when(caseRepository.findById(1L)).thenReturn(Optional.of(new CourtCase()));
        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));

        stageService.saveCourtStage(courtStage);

        verify(stageRepository).findByCourtCase_CourtCaseIdOrderByStart(1L);
    }

    @Test
    public void testSaveCourtStage_whenValid_thenShouldSetDocumentCreationDate() {
        CourtStage courtStage = buildCourtStage();
        courtStage.getDocuments().clear();
        Document document = spy(Document.class);
        courtStage.setDocuments(Collections.singletonList(document));

        when(stageRepository.findByCourtCase_CourtCaseIdOrderByStart(1L))
                .thenReturn(Collections.singletonList(courtStage));
        when(caseRepository.findById(1L)).thenReturn(Optional.of(new CourtCase()));
        when(courtRepository.findById(1L)).thenReturn(Optional.of(new Court()));

        stageService.saveCourtStage(courtStage);

        assertNotNull(document.getCreationDate());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteCourtStage_whenCourtStageNotExists_thenFail() {
        when(stageRepository.findById(1L)).thenReturn(Optional.empty());
        stageService.deleteCourtStage(1L);
    }

    private CourtStage buildCourtStage() {
        CourtStage courtStage = spy(CourtStage.class);
        Document document = new Document();
        Court court = new Court(1L);
        CourtCase courtCase = new CourtCase(1L);
        courtStage.addDocument(document);
        courtStage.setCourt(court);
        courtStage.setCourtCase(courtCase);
        return courtStage;
    }
}
