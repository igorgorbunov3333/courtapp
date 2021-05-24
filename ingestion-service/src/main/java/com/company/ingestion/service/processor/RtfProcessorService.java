package com.company.ingestion.service.processor;

import com.company.ingestion.domain.dto.DocumentDataDto;

public interface RtfProcessorService {

    DocumentDataDto process(String document);

}
