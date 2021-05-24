package com.company.ingestion.controller;

import com.company.ingestion.service.ingestion.IngestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/documents")
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping("/rtf")
    public void ingestRtf(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();

        ingestionService.ingestRtf(inputStream);
    }

    @PostMapping("/xml")
    public void ingestXml(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();

        ingestionService.ingestXml(inputStream);
    }
}
