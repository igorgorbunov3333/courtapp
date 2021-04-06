package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.repository.DocumentRepository;
import com.company.courtmanagement.service.DocumentService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository docRepository;
    private CourtStageRepository stageRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository docRepository, CourtStageRepository stageRepository) {
        this.docRepository = docRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public Document findDocument(long documentId) {
            Optional<Document> documentOptional = docRepository.findById(documentId);
            return documentOptional.orElseThrow(()
                    -> new ResourceNotFoundException("No document with id: " + documentId));
    }

    @Override
    public Document saveDocument(Document document) {
        if (document == null) {
            throw new ResourceNotValidException("Document should not be null");
        }
        document.validate();
        CourtStage courtStage = findCourtStage(document.getCourtStage().getCourtStageId());
        document.setCreationDate(LocalDate.now());
        courtStage.addDocument(document);
        CourtStage savedCourtStage = stageRepository.save(courtStage);

        List<Document> sortedDocuments = new ArrayList<>(savedCourtStage.getSortedDocuments());
        return sortedDocuments.get(sortedDocuments.size() - 1);
    }

    private CourtStage findCourtStage(long stageId) {
        Optional<CourtStage> optionalStage = stageRepository.findById(stageId);
        return optionalStage.orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.STAGE_NOT_EXIST + stageId));
    }
}
