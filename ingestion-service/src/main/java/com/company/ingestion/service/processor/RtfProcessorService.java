package com.company.ingestion.service.processor;

import com.company.ingestion.domain.dto.DocumentDataDto;
import org.springframework.stereotype.Service;

@Service
public interface RtfProcessorService {

    DocumentDataDto process(String document);

}
