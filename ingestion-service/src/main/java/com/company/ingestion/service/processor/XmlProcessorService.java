package com.company.ingestion.service.processor;

import java.io.IOException;
import java.io.InputStream;

public interface XmlProcessorService {

    void process(InputStream inputStream) throws IOException;

}
