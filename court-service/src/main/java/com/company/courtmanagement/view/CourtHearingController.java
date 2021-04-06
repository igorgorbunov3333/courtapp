package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtHearing;
import com.company.courtmanagement.service.CourtHearingService;
import com.company.courtmanagement.view.dto.input.CourtHearingInputDto;
import com.company.courtmanagement.view.dto.output.CourtHearingOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtHearingMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(UriPathHelper.COURT_HEARINGS)
@Api(tags = "Court hearings", description = "REST API for court hearings")
public class CourtHearingController {
    private CourtHearingMapper mapper;
    private CourtHearingService hearingService;

    @Autowired
    public CourtHearingController(CourtHearingMapper mapper, CourtHearingService hearingService) {
        this.mapper = mapper;
        this.hearingService = hearingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Court hearing created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save court hearing")
    public CourtHearingOutputDto saveCourtHearing(@Valid @RequestBody CourtHearingInputDto inputDto) {
        CourtHearing hearingToSave = mapper.mapToModel(inputDto);
        CourtHearing savedHearing = hearingService.saveCourtHearing(hearingToSave);
        return mapper.mapToDto(savedHearing);
    }
}
