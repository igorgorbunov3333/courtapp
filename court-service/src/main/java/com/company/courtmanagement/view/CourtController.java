package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.service.CourtService;
import com.company.courtmanagement.view.dto.input.CourtInputDto;
import com.company.courtmanagement.view.dto.output.CourtOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(UriPathHelper.COURTS)
@Api(tags = "Courts", description = "REST API for courts")
public class CourtController {
    private ModelMapper mapper;
    private CourtService courtService;

    @Autowired
    public CourtController(ModelMapper mapper, CourtService courtService) {
        this.mapper = mapper;
        this.courtService = courtService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Case created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save court case")
    public CourtOutputDto saveCourt(@Valid @RequestBody CourtInputDto input) {
        Court courtToSave = mapper.map(input, Court.class);
        Court savedCourt = courtService.saveCourt(courtToSave);
        return mapper.map(savedCourt, CourtOutputDto.class);
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Courts successfully obtained"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get courts")
    public Collection<Court> getCourts() {
        return courtService.findAllCourts();
    }
}
