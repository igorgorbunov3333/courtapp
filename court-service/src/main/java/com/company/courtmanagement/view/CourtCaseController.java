package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.service.CourtCaseService;
import com.company.courtmanagement.view.dto.input.CourtCaseInputDto;
import com.company.courtmanagement.view.dto.output.CourtCaseOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtCaseMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UriPathHelper.COURT_CASES)
@Api(tags = "Court cases", description = "REST API for court cases")
public class CourtCaseController {
    private CourtCaseMapper courtCaseMapper;
    private CourtCaseService courtCaseService;

    @Autowired
    public CourtCaseController(
            CourtCaseMapper courtCaseMapper, CourtCaseService courtCaseService) {
        this.courtCaseMapper = courtCaseMapper;
        this.courtCaseService = courtCaseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Court case created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save court case")
    public CourtCaseOutputDto saveCourtCase(
            @ApiParam(name = "Court case to save", value = "Save court case")
            @Valid @RequestBody CourtCaseInputDto inputDto) {
        CourtCase courtCase = courtCaseMapper.mapToModel(inputDto);
        CourtCase savedCourtCase = courtCaseService.saveCourtCase(courtCase);
        return courtCaseMapper.mapToDto(savedCourtCase);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Court case successfully deleted"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Delete court case")
    public void deleteCourtCase(@Valid @Positive @PathVariable(name = "id") long courtCaseId) {
        courtCaseService.deleteCourtCase(courtCaseId);
    }

    @GetMapping(path = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Court case successfully obtained"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get court case")
    public CourtCaseOutputDto getCourtCase(@Valid @Positive @PathVariable(name = "id") long courtCaseId) {
        CourtCase courtCase = courtCaseService.findCourtCase(courtCaseId);
        return courtCaseMapper.mapToDto(courtCase);
    }
}
