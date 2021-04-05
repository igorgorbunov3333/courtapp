package com.company.ingestion.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder(setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentDataDto {

    private final Map<String, String> articlesToRegulatoryDocuments;
    private final String introductoryPart;
    private final String motivationalPart;
    private final String operativePart;

}
