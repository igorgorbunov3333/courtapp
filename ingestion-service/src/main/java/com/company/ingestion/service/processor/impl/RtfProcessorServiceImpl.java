package com.company.ingestion.service.processor.impl;

import com.company.ingestion.domain.RegulatoryDocumentAbbreviation;
import com.company.ingestion.domain.dto.DocumentDataDto;
import com.company.ingestion.domain.entity.RegulatoryDocumentType;
import com.company.ingestion.repository.RegulatoryDocumentTypeRepository;
import com.company.ingestion.service.cache.Cache;
import com.company.ingestion.service.processor.RtfProcessorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RtfProcessorServiceImpl implements RtfProcessorService {

    private static final String PARAGRAPH_PREFIX_ARTICLE_NUMBER = "(ст\\.|п\\.)\\s*((\\d+-\\d+)|(\\d+))\\s*";
    private static final String REGULATORY_DOC = "[а-яА-Я]+";
    private static final String REGULATORY_DOC_END_OF_STRING = REGULATORY_DOC + "$";
    private static final String PARAGRAPH_PREFIX_ARTICLE_NUMBER_REGULATORY_DOC = PARAGRAPH_PREFIX_ARTICLE_NUMBER + REGULATORY_DOC;

    private static final String EMPTY_STRING = "";
    private static final String KEY_REGULATORY_DOCUMENT = "document";

    private Cache<String, List<RegulatoryDocumentType>> cache;

    @Autowired
    private RegulatoryDocumentTypeRepository regulatoryDocumentTypeRepository;

    @Override
    public DocumentDataDto process(String document) {
        String[] rows = document.split("\n");
        Map<String, String> articlesToRegulatoryDocuments = findArticlesWithRegulatoryDocuments(rows);

        return DocumentDataDto.builder()
                .setArticlesToRegulatoryDocuments(articlesToRegulatoryDocuments)
                .build();
    }

    private Map<String, String> findArticlesWithRegulatoryDocuments(String[] rows) {
        Map<String, String> result = new HashMap<>();
        for (String row : rows) {
            Map<String, String> articlesToRegulatoryDocuments = processRow(row);
            Map<String, String> mapToAddToResult = getNewArticlesOrArticlesWithNewRegulatoryDocument(
                    result, articlesToRegulatoryDocuments);
            result.putAll(mapToAddToResult);
        }
        return result;
    }

    private Map<String, String> getNewArticlesOrArticlesWithNewRegulatoryDocument(Map<String, String> result,
                                                                                  Map<String, String> articlesToRegulatoryDocuments) {
        Map<String, String> mapToAddToResult = new HashMap<>();
        for (Map.Entry<String, String> entry : articlesToRegulatoryDocuments.entrySet()) {
            String regulatoryDocument = result.get(entry.getKey());

            if (StringUtils.isNotBlank(regulatoryDocument)) {
                continue;
            }

            mapToAddToResult.put(entry.getKey(), entry.getValue());
        }
        return mapToAddToResult;
    }

    private Map<String, String> processRow(String row) {
        Map<String, String> articlesToRegulatoryDocuments = new HashMap<>();
        Matcher articleWithNextWordMatcher = getMatcher(row, PARAGRAPH_PREFIX_ARTICLE_NUMBER_REGULATORY_DOC);

        cache = new Cache<>(regulatoryDocumentTypeRepository::findAll);
        Set<String> availableRegulatoryDocuments = getAvailableRegulatoryDocuments();
        while (articleWithNextWordMatcher.find()) {
            String articleWithNextWord = articleWithNextWordMatcher.group();
            String articleNumber = findArticleNumber(articleWithNextWord);
            if (!articleNumber.isBlank()) {
                String articleNumberWithoutSpaces = articleNumber.replaceAll(" ", "");
                String regulatoryDocument = findRegulatoryDocument(articleWithNextWord, availableRegulatoryDocuments);
                articlesToRegulatoryDocuments.put(articleNumberWithoutSpaces, regulatoryDocument);
            }
        }
        return articlesToRegulatoryDocuments;
    }

    private Set<String> getAvailableRegulatoryDocuments() {
        List<RegulatoryDocumentType> regulatoryDocumentTypes = cache.get(KEY_REGULATORY_DOCUMENT);
        return regulatoryDocumentTypes.stream()
                .map(RegulatoryDocumentType::getType)
                .collect(Collectors.toSet());
    }

    private String findArticleNumber(String articleWithNextWord) {
        Matcher articleNumberMatcher = getMatcher(articleWithNextWord, PARAGRAPH_PREFIX_ARTICLE_NUMBER);
        if (articleNumberMatcher.find()) {
            return articleNumberMatcher.group();
        }
        return EMPTY_STRING;
    }

    private String findRegulatoryDocument(String articleWithNextWord, Set<String> availableRegulatoryDocuments) {
        Matcher nextWordMatcher = getMatcher(articleWithNextWord, REGULATORY_DOC_END_OF_STRING);
        if (nextWordMatcher.find()) {
            String nextWord = nextWordMatcher.group();

            for (String regulatoryDocument : availableRegulatoryDocuments) {
                if (regulatoryDocument.startsWith(nextWord)) {

                }
            }
            Optional<RegulatoryDocumentAbbreviation> regulatoryDocument = RegulatoryDocumentAbbreviation.of(nextWord);
            if (regulatoryDocument.isPresent()) {
                return regulatoryDocument.get().toString();
            }
        }
        return EMPTY_STRING;
    }

    private Matcher getMatcher(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }

}
