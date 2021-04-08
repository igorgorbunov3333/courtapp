package com.company.ingestion.service.processor.impl;

import com.company.ingestion.domain.entity.RegulatoryDocumentType;
import com.company.ingestion.domain.xml.Rna;
import com.company.ingestion.repository.RegulatoryDocumentTypeRepository;
import com.company.ingestion.service.processor.XmlProcessorService;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class XmlProcessorServiceImpl implements XmlProcessorService {

    private RegulatoryDocumentTypeRepository regulatoryDocumentTypeRepository;

    @Override
    public void process(InputStream inputStream) throws IOException {
        String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(xmlModule);
        xml = xml.replace("\n", "").replace("\t", "");
        Rna rna = xmlMapper.readValue(xml, Rna.class);

        List<RegulatoryDocumentType> regularDocumentTypes = rna.getDatabase().stream()
                .flatMap(database -> database.getDocument().stream())
                .flatMap(document -> document.getItem().stream())
                .filter(item -> "type".equals(item.getName()))
                .map(item -> item.getText().getValue())
                .distinct()
                .map(docType -> new RegulatoryDocumentType(null, docType))
                .collect(Collectors.toList());

        regulatoryDocumentTypeRepository.saveAll(regularDocumentTypes);
    }

}
