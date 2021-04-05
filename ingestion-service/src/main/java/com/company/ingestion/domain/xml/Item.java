package com.company.ingestion.domain.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Item {

    private Text text;
    private List<TextList> textlist;
    private String name;

}
