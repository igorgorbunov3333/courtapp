package com.company.ingestion.service.ingestion.impl;

import com.company.ingestion.domain.dto.DocumentDataDto;
import com.company.ingestion.service.processor.impl.RtfProcessorServiceImpl;
import com.rtfparserkit.converter.text.StringTextConverter;
import com.rtfparserkit.parser.RtfStreamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class DocumentAnalyzerServiceImplTest {

    @InjectMocks
    private RtfProcessorServiceImpl documentAnalyzerService;

    @Test
    void process_WhenDocumentWithArticlesAndRegulatoryDocuments_ThenFindAllRegulatoryDocumentsAndArticles() {
        String document = getText();

        if (document == null) {
            fail("Cannot fetch text from document");
        }
        DocumentDataDto actualDocumentDataDto = documentAnalyzerService.process(document);
        assertThat(actualDocumentDataDto.getArticlesToRegulatoryDocuments()).hasSize(7)
                .containsExactly(Map.entry("ст.16", ""),
                        Map.entry("ст.130", ""),
                        Map.entry("ст.7", ""),
                        Map.entry("ст.245", ""),
                        Map.entry("ст.4", ""),
                        Map.entry("ст.40-1", ""),
                        Map.entry("п.5", ""));
    }

    private String getText() {
        String fileName = "inputs/ingestion/service/analyzer/court_document.rtf";
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            StringTextConverter converter = new StringTextConverter();
            converter.convert(new RtfStreamSource(inputStream));
            return converter.getText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
