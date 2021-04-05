package com.company.ingestion.domain.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Rna {

    @JacksonXmlProperty(localName = "database")
    private List<Database> database;

}
