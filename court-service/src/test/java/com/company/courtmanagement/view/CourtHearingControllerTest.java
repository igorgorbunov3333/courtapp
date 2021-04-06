package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtHearing;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.service.CourtHearingService;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.input.CourtHearingInputDto;
import com.company.courtmanagement.view.dto.input.DocumentInputDto;
import com.company.courtmanagement.view.dto.output.CourtHearingOutputDto;
import com.company.courtmanagement.view.dto.output.CourtStageOutputDto;
import com.company.courtmanagement.view.dto.output.DocumentOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtHearingMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CourtHearingController.class)
@Import({ModelMapperConfig.class, CourtHearingMapper.class})
public class CourtHearingControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourtHearingService courtHearingService;
    @Autowired
    private CourtHearingMapper courtHearingMapper;
    private HttpHeaders httpHeaders;
    private ObjectMapper mapper;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSaveCourtHearing_whenSavedSuccessfully_thenReturn201WithSavedEntity() throws Exception {
        CourtHearingInputDto inputDto = new CourtHearingInputDto();
        inputDto.setDocuments(List.of(new DocumentInputDto()));
        inputDto.setCourtStage(1L);

        CourtHearing courtHearing = new CourtHearing();
        courtHearing.setCourtStage(new CourtStage(1L));
        courtHearing.setDocuments(List.of(new Document()));

        CourtHearing savedHearing = new CourtHearing(1L);
        CourtStage courtStage = new CourtStage(1L);
        courtStage.setStart(LocalDate.now());
        savedHearing.setCourtStage(courtStage);
        savedHearing.setDocuments(List.of(new Document()));
        when(courtHearingService.saveCourtHearing(courtHearing)).thenReturn(savedHearing);

        CourtHearingOutputDto expectedDto = new CourtHearingOutputDto();
        expectedDto.setCourtHearingId(1L);
        CourtStageOutputDto courtStageOutputDto = new CourtStageOutputDto();
        courtStageOutputDto.setCourtStageId(1L);
        courtStageOutputDto.setStart(LocalDate.now());
        expectedDto.setCourtStage(1L);
        expectedDto.setDocuments(List.of(new DocumentOutputDto()));

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_HEARINGS)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    public void testSaveCourtHearing_whenResourceNotValid_thenReturn400() throws Exception{
        CourtHearingInputDto inputDto = new CourtHearingInputDto();
        inputDto.setCourtStage(1L);

        CourtHearing courtHearing = new CourtHearing();
        courtHearing.setCourtStage(new CourtStage(1L));

        when(courtHearingService.saveCourtHearing(courtHearing)).thenThrow(ResourceNotValidException.class);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_HEARINGS)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }
}
