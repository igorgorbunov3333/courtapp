package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.service.CourtStageService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.input.CourtStageInputDto;
import com.company.courtmanagement.view.dto.output.CourtCaseOutputDto;
import com.company.courtmanagement.view.dto.output.CourtOutputDto;
import com.company.courtmanagement.view.dto.output.CourtStageOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtStageMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebMvcTest(CourtStageController.class)
@Import({ModelMapperConfig.class, CourtStageMapper.class})
public class CourtStageControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourtStageService courtStageService;
    @Autowired
    private ModelMapper modelMapper;
    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
    }

    @Test
    public void testSaveCourtStage_whenSave_thenReturn201WithSavedCourtStage() throws Exception {
        LocalDate ld = LocalDate.now();

        CourtStageInputDto inputDto = new CourtStageInputDto();
        inputDto.setStart(ld);
        inputDto.setCourtCase(1L);
        inputDto.setCourt(1L);

        CourtStageOutputDto expectedDto = new CourtStageOutputDto();
        expectedDto.setCourtStageId(1L);
        expectedDto.setStart(ld);

        CourtCaseOutputDto courtCaseOutputDto = new CourtCaseOutputDto();
        courtCaseOutputDto.setAccountId(1L);
        expectedDto.setCourtCase(1L);
        courtCaseOutputDto.setCourtStages(Collections.singleton(expectedDto));

        CourtOutputDto courtOutputDto = new CourtOutputDto();
        courtOutputDto.setCourtId(1L);
        courtOutputDto.setName("some");
        expectedDto.setCourt(courtOutputDto);

        Court court = new Court();
        court.setCourtId(1L);
        court.setName("some");

        CourtCase courtCase = new CourtCase();
        courtCase.setCourtCaseId(1L);
        courtCase.setAccountId(1L);

        CourtStage courtStage = new CourtStage();
        courtStage.setCourtStageId(1L);
        courtStage.setStart(ld);
        courtStage.setCourt(court);
        courtStage.setCourtCase(courtCase);

        courtCase.setCourtStages(Collections.singleton(courtStage));

        when(courtStageService.saveCourtStage(any(CourtStage.class))).thenReturn(courtStage);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURT_STAGES)
                .headers(httpHeaders)
                .content(mapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    public void testDeleteCourtStage_whenDeletedSuccessfully_thenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(UriPathHelper.COURT_STAGES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    public void testDeleteCourtStage_whenCourtStageNotExists_thenReturn404() throws Exception {
        doThrow(ResourceNotFoundException.class).when(courtStageService).deleteCourtStage(1L);

        mvc.perform(MockMvcRequestBuilders.delete(UriPathHelper.COURT_STAGES + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }
}
