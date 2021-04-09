package com.company.ingestion.service.processor.impl;

import com.company.ingestion.domain.entity.RegulatoryDocumentType;
import com.company.ingestion.domain.xml.Document;
import com.company.ingestion.domain.xml.Item;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class XmlProcessorServiceImpl implements XmlProcessorService {

    private final RegulatoryDocumentTypeRepository regulatoryDocumentTypeRepository;

    @Override
    public void process(InputStream inputStream) throws IOException {
        String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(xmlModule);
        xml = xml.replace("\n", "").replace("\t", "");
        Rna rna = xmlMapper.readValue(xml, Rna.class);

        List<Document> documents = rna.getDatabase().stream()
                .flatMap(database -> database.getDocument().stream())
                .collect(Collectors.toList());

        List<RegulatoryDocumentType> regulatoryDocumentTypesToSave = new ArrayList<>();
        for (Document document : documents) {
            List<Item> items = document.getItem();
            String type = null;
            String name = null;
            for (Item item : items) {
                if ("type".equals(item.getName())) {
                    type = item.getText().getValue();
                } else if ("name".equals(item.getName())) {
                    name = item.getText().getValue();
                }
            }
            regulatoryDocumentTypesToSave.add(new RegulatoryDocumentType(null, type, name));
        }

        List<RegulatoryDocumentType> notNullableRegulatoryDocumentTypes = regulatoryDocumentTypesToSave.stream()
                .filter(doc -> doc.getName() != null)
                .filter(doc -> doc.getType() != null)
                .collect(Collectors.toList());

        regulatoryDocumentTypeRepository.saveAll(notNullableRegulatoryDocumentTypes);
    }

}
