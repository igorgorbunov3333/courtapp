package com.company.ingestion.domain;

import java.util.Arrays;
import java.util.Optional;

public enum RegulatoryDocumentAbbreviation {

    CODE_OF_ADMINISTRATIVE_OFFENCES("КУпАП");

    private final String documentName;

    RegulatoryDocumentAbbreviation(String documentName) {
        this.documentName = documentName;
    }

    public static Optional<RegulatoryDocumentAbbreviation> of(String documentName) {
        return Arrays.stream(values())
                .filter(value -> value.toString().equalsIgnoreCase(documentName)
                        || documentName.startsWith(value.toString()))
                .findFirst();
    }

    @Override
    public String toString() {
        return documentName;
    }
}
