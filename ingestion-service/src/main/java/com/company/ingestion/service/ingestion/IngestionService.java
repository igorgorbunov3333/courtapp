package com.company.ingestion.service.ingestion;

import java.io.IOException;
import java.io.InputStream;

public interface IngestionService {

    void ingestRtf(InputStream inputStream) throws IOException;

    void ingestXml(InputStream inputStream) throws IOException;
}
