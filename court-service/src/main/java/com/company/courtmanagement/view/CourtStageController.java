package com.company.courtmanagement.view;

import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.service.CourtStageService;
import com.company.courtmanagement.view.dto.input.CourtStageInputDto;
import com.company.courtmanagement.view.dto.output.CourtStageOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import com.company.courtmanagement.view.util.mapper.CourtStageMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UriPathHelper.COURT_STAGES)
@Api(tags = "Court stages", description = "REST API for court stages")
public class CourtStageController {
    private CourtStageMapper mapper;
    private CourtStageService stageService;

    @Autowired
    public CourtStageController(CourtStageMapper mapper, CourtStageService stageService) {
        this.mapper = mapper;
        this.stageService = stageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Court stages created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save court stage")
    public CourtStageOutputDto saveCourtStage(@Valid @RequestBody CourtStageInputDto input) {
        CourtStage courtStage = mapper.mapToModel(input);
        CourtStage savedStage = stageService.saveCourtStage(courtStage);
        return mapper.mapToDto(savedStage);
    }

    @DeleteMapping(path = "/{courtStageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Court stage successfully deleted"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Delete court stage")
    public void deleteCourtStage(@Valid @Positive @PathVariable(name = "courtStageId") long stageId) {
        stageService.deleteCourtStage(stageId);
    }
}
