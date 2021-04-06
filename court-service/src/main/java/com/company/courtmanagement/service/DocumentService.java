package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Document;

public interface DocumentService {
    Document findDocument(long documentId);

    Document saveDocument(Document document);
}
