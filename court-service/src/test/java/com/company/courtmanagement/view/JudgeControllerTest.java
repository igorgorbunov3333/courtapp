package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.service.JudgeService;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.input.JudgeInputDto;
import com.company.courtmanagement.view.dto.output.CourtOutputDto;
import com.company.courtmanagement.view.dto.output.JudgeOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
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
import org.testcontainers.shaded.com.fasterxml.jackson.annotation.JsonInclude;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(JudgeController.class)
@Import(ModelMapperConfig.class)
public class JudgeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JudgeService judgeService;
    @Autowired
    private ModelMapper modelMapper;
    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
    }

    @Test
    public void testSaveJudge_whenSaveSuccessfully_thenReturn201WithSavedCourt() throws Exception {
        JudgeOutputDto expectedDto = new JudgeOutputDto();
        expectedDto.setJudgeId(1L);
        expectedDto.setFirstName("firstName");

        CourtOutputDto courtOutputDto = new CourtOutputDto();
        courtOutputDto.setCourtId(1L);
        courtOutputDto.setName("some");
        expectedDto.setCourt(courtOutputDto);

        Judge judge = new Judge();
        judge.setJudgeId(1L);
        judge.setFirstName("firstName");
        Court court = new Court();
        court.setCourtId(1L);
        court.setName("some");
        judge.setCourt(court);

        when(judgeService.saveJudge(any(Judge.class))).thenReturn(judge);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURTS + "/1" + UriPathHelper.JUDGES)
                .headers(httpHeaders)
                .content(new ObjectMapper().writeValueAsString(buildInputDto())))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(expectedDto)));
    }

    @Test
    public void testSaveJudge_whenNotValidInput_thenFail() throws Exception {
        JudgeInputDto inputDto = buildInputDto();
        inputDto.setSurname(null);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURTS + "/1" + UriPathHelper.JUDGES)
                .headers(httpHeaders)
                .content(new ObjectMapper().writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    private JudgeInputDto buildInputDto() {
        JudgeInputDto inputDto = new JudgeInputDto();
        inputDto.setFirstName("some");
        inputDto.setSurname("surname");
        inputDto.setPatronymic("patronymic");
        return inputDto;
    }
}
