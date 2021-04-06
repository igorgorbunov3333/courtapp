package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.service.DocumentService;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.input.DocumentInputDto;
import com.company.courtmanagement.view.dto.output.DocumentOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentController.class)
@Import({ModelMapperConfig.class})
public class DocumentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private DocumentService documentService;
    private HttpHeaders httpHeaders;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSaveDocument_whenSave_thenReturn201WithSavedDocument() throws Exception {
        Document documentToSave = prepareDocumentToSave();

        Document savedDocument = prepareSavedDocument();
        when(documentService.saveDocument(documentToSave)).thenReturn(savedDocument);

        DocumentOutputDto expectedDto = new DocumentOutputDto();
        expectedDto.setCourtStage(1L);
        expectedDto.setDocumentId(1L);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_STAGES + "/1" + UriPathHelper.DOCUMENTS)
                .headers(httpHeaders)
                .content(new ObjectMapper().writeValueAsString(buildInputDto())))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string(objectMapper.writeValueAsString(expectedDto)));
    }

    @Test
    public void testSaveDocument_whenCourtStageIdNotValid_thenReturn400() throws Exception {
        Document documentToSave = prepareDocumentToSave();

        when(documentService.saveDocument(documentToSave)).thenThrow(ResourceNotValidException.class);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_STAGES + "/1" + UriPathHelper.DOCUMENTS)
                .headers(httpHeaders)
                .content(new ObjectMapper().writeValueAsString(buildInputDto())))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    private Document prepareDocumentToSave() {
        Document documentToSave = new Document();
        documentToSave.setCourtStage(new CourtStage(1L));
        return documentToSave;
    }

    private Document prepareSavedDocument() {
        Document savedDocument = new Document();
        savedDocument.setDocumentId(1L);
        CourtStage courtStage = new CourtStage();
        courtStage.setCourtStageId(1L);
        courtStage.setDocuments(new LinkedList<>(Collections.singletonList(savedDocument)));
        courtStage.setStart(LocalDate.now());
        savedDocument.setCourtStage(courtStage);
        return savedDocument;
    }

    private DocumentInputDto buildInputDto() {
        DocumentInputDto dto = new DocumentInputDto();
        dto.setCourtStage(1L);
        return dto;
    }
}
