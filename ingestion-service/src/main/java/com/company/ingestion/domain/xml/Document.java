package com.company.ingestion.domain.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Document {

    private List<Item> item;

}
