package com.company.ingestion.service.ingestion.impl;

import com.company.ingestion.domain.dto.DocumentDataDto;
import com.company.ingestion.service.ingestion.IngestionService;
import com.company.ingestion.service.processor.RtfProcessorService;
import com.company.ingestion.service.processor.XmlProcessorService;
import com.rtfparserkit.converter.text.StringTextConverter;
import com.rtfparserkit.parser.RtfStreamSource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class IngestionServiceImpl implements IngestionService {

    private final RtfProcessorService rtfProcessorService;
    private final XmlProcessorService xmlProcessorService;

    @Override
    public void ingestRtf(InputStream documentInputStream) throws IOException {
        StringTextConverter converter = new StringTextConverter();
        converter.convert(new RtfStreamSource(documentInputStream));
        String documentText = converter.getText();

        DocumentDataDto documentDataDto = rtfProcessorService.process(documentText);
    }

    @Override
    public void ingestXml(InputStream inputStream) throws IOException {
        xmlProcessorService.process(inputStream);
    }
}
