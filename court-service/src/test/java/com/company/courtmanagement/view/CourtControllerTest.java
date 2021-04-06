package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.service.CourtService;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.view.config.ModelMapperConfig;
import com.company.courtmanagement.view.dto.AddressDto;
import com.company.courtmanagement.view.dto.input.CourtInputDto;
import com.company.courtmanagement.view.dto.output.CourtOutputDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CourtController.class)
@Import(ModelMapperConfig.class)
public class CourtControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourtService courtService;
    @Autowired
    private ModelMapper modelMapper;
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
    public void testSaveCourt_whenNotValidAddress_thenReturn400() throws Exception {
        CourtInputDto inputDto = new CourtInputDto();
        when(courtService.saveCourt(any(Court.class))).thenThrow(ResourceNotValidException.class);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURTS)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSaveCourt_whenSave_thenReturn201WithSavedCourt() throws Exception {
        CourtInputDto inputDto = new CourtInputDto();
        inputDto.setName("name");
        inputDto.setAddress(buildAddressDto());
        inputDto.setProceeding(Court.Proceeding.COMMON);

        Court savedCourt = buildCourt();
        savedCourt.setCourtId(1L);

        when(courtService.saveCourt(buildCourt())).thenReturn(savedCourt);

        CourtOutputDto expectedDto = new CourtOutputDto();
        expectedDto.setCourtId(1L);
        expectedDto.setName("name");
        expectedDto.setAddress(buildAddressDto());
        expectedDto.setProceeding(Court.Proceeding.COMMON);

        mvc.perform(MockMvcRequestBuilders.post(UriPathHelper.COURTS)
                .headers(httpHeaders)
                .content(new ObjectMapper().writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string(new ObjectMapper().writeValueAsString(expectedDto)));
    }

    private Court buildCourt() {
        Court court = new Court();
        court.setName("name");

        Address address = new Address();
        address.setBuilding("building");
        address.setRegion("region");
        address.setCity("city");
        court.setAddress(address);
        court.setProceeding(Court.Proceeding.COMMON);
        return court;
    }

    private AddressDto buildAddressDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setBuilding("building");
        addressDto.setRegion("region");
        addressDto.setCity("city");
        return addressDto;
    }
}
