package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.service.CourtCaseService;
import com.company.courtmanagement.service.CourtStageService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.input.CourtCaseInputDto;
import com.company.courtmanagement.view.dto.input.CourtStageInputDto;
import com.company.courtmanagement.view.dto.output.CourtCaseOutputDto;
import com.company.courtmanagement.view.dto.output.CourtOutputDto;
import com.company.courtmanagement.view.dto.output.CourtStageOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtCaseMapper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebMvcTest(CourtCaseController.class)
@Import({ModelMapperConfig.class, CourtCaseMapper.class})
public class CourtCaseControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourtCaseService courtCaseService;
    @MockBean
    private CourtStageService courtStageService;
    private HttpHeaders httpHeaders;
    @Autowired
    private CourtCaseMapper courtCaseMapper;
    private ObjectMapper mapper;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDeleteCourtCase_whenSuccessfullyDeleted_thenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(UriPathHelper.COURT_CASES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    public void testDeleteCourtCase_whenResourceNotFound_thenReturn404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(courtCaseService).deleteCourtCase(1L);

        mvc.perform(MockMvcRequestBuilders.delete(UriPathHelper.COURT_CASES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void testSaveCourtCase_whenAccountIdNull_thenReturn400() throws Exception {
        CourtCaseInputDto inputDto = new CourtCaseInputDto();

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_CASES)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSaveCourtCase_whenResourceNotValid_thenReturn400() throws Exception {
        CourtCase caseToSave = buildCourtCaseToSave();
        when(courtCaseService.saveCourtCase(caseToSave)).thenThrow(ResourceNotValidException.class);

        CourtCaseInputDto inputDto = buildCourtCaseDto();

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_CASES)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSaveCourtCase_whenSuccessfullySave_thenReturn201WithSavedCourtCase() throws Exception {
        CourtCase courtCaseToSave = buildCourtCaseToSave();
        CourtCase savedCourtCase = buildSavedCourtCase();

        when(courtCaseService.saveCourtCase(courtCaseToSave)).thenReturn(savedCourtCase);

        CourtCaseInputDto inputDto = buildCourtCaseDto();
        CourtCaseOutputDto expectedDto = buildExpectedDto();

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_CASES)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    public void testGetCourtCase_whenGot_thenReturn200WithJsonResponse() throws Exception {
        CourtCaseOutputDto expectedDto = buildExpectedDto();
        CourtCase savedCourtCase = buildSavedCourtCase();
        when(courtCaseService.findCourtCase(1L)).thenReturn(savedCourtCase);

        mvc.perform(MockMvcRequestBuilders.get(UriPathHelper.COURT_CASES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .string(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    public void testGetCourtCase_whenResourceNotFound_thenReturn404() throws Exception {
        when(courtCaseService.findCourtCase(1L)).thenThrow(ResourceNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get(UriPathHelper.COURT_CASES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    private CourtCase buildCourtCaseToSave() {
        CourtCase courtCase = new CourtCase();
        CourtStage courtStage = new CourtStage();
        courtStage.setCourtStageId(1L);
        courtCase.setCourtStages(new ArrayList<>(List.of(courtStage)));
        courtCase.setAccountId(1L);
        return courtCase;
    }

    private CourtCase buildSavedCourtCase() {
        CourtStage courtStage = new CourtStage();
        courtStage.setCourtStageId(1L);
        courtStage.setCourt(new Court(1L));
        courtStage.setStart(LocalDate.now());

        CourtCase savedCourtCase = new CourtCase();
        savedCourtCase.setCourtCaseId(1L);
        savedCourtCase.setCourtStages(new ArrayList<>(Collections.singletonList(courtStage)));
        savedCourtCase.setAccountId(1L);
        return savedCourtCase;
    }

    private CourtCaseInputDto buildCourtCaseDto() {
        CourtCaseInputDto inputDto = new CourtCaseInputDto();
        inputDto.setAccountId(1L);
        CourtStageInputDto courtStageDto = new CourtStageInputDto();
        courtStageDto.setCourtStageId(1L);
        inputDto.setCourtStages(new ArrayList<>(Collections.singletonList(courtStageDto)));
        return inputDto;
    }

    private CourtCaseOutputDto buildExpectedDto() {
        CourtCaseOutputDto expectedDto = new CourtCaseOutputDto();
        expectedDto.setCourtCaseId(1L);
        expectedDto.setAccountId(1L);

        CourtStageOutputDto courtStageOutputDto = new CourtStageOutputDto();
        courtStageOutputDto.setCourtStageId(1L);
        courtStageOutputDto.setStart(LocalDate.now());

        CourtOutputDto courtDto = new CourtOutputDto();
        courtDto.setCourtId(1L);
        courtStageOutputDto.setCourt(courtDto);
        expectedDto.setCourtStages(Collections.singletonList(courtStageOutputDto));
        return expectedDto;
    }
}
