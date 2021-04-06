package com.company.courtmanagement.service;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.repository.DocumentRepository;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.DocumentServiceImpl;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DocumentServiceTest {
    private CourtStageRepository stageRepository = mock(CourtStageRepository.class);
    private final DocumentService documentService = new DocumentServiceImpl(
            mock(DocumentRepository.class),
            stageRepository);

    @Test(expected = ResourceNotValidException.class)
    public void testSaveDocument_whenNull_thenFail() {
        documentService.saveDocument(null);
        verifyZeroInteractions(stageRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveDocument_whenNoCourtStage_thenFail() {
        Document document = new Document();
        documentService.saveDocument(document);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveDocument_whenNoCourtStage_Id_thenFail() {
        Document document = new Document();
        document.setCourtStage(new CourtStage());
        documentService.saveDocument(document);
    }

    @Test
    public void testSaveDocument_whenSave_thenReturnExactlyTheSameSavedDocument() {
        CourtStage courtStage = new CourtStage();
        courtStage.setCourtStageId(1L);
        Document document = new Document();
        document.setDocumentId(1L);
        document.setCreationDate(LocalDate.now().minus(1, ChronoUnit.MONTHS));
        courtStage.addDocument(document);

        Document document1 = new Document();
        document.setDocumentId(2L);
        document1.setCreationDate(LocalDate.now().minus(2, ChronoUnit.MONTHS));
        courtStage.addDocument(document1);

        when(stageRepository.findById(1L)).thenReturn(Optional.of(courtStage));
        when(stageRepository.save(courtStage)).thenReturn(courtStage);

        Document documentToSave = new Document();
        documentToSave.setCourtStage(new CourtStage(1L));
        Document actualDocument = documentService.saveDocument(documentToSave);

        assertEquals(actualDocument.getCreationDate(), LocalDate.now());
    }
}