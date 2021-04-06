package com.company.courtmanagement.view;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.service.JudgeService;
import com.company.courtmanagement.view.dto.input.JudgeInputDto;
import com.company.courtmanagement.view.dto.output.JudgeOutputDto;
import com.company.courtmanagement.view.util.UriPathHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UriPathHelper.COURTS + "/{courtId}" + UriPathHelper.JUDGES)
@Api(tags = "Judges", description = "REST API for judges")
public class JudgeController {
    private ModelMapper modelMapper;
    private JudgeService judgeService;

    @Autowired
    public JudgeController(ModelMapper modelMapper, JudgeService judgeService) {
        this.modelMapper = modelMapper;
        this.judgeService = judgeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Court case created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Save judges")
    public JudgeOutputDto saveCourt(@Valid @RequestBody JudgeInputDto judgeInputDto,
                                    @Valid @Positive @PathVariable long courtId) {
        Judge judge = modelMapper.map(judgeInputDto, Judge.class);
        judge.setCourt(new Court(courtId));
        Judge savedJudge = judgeService.saveJudge(judge);
        return modelMapper.map(savedJudge, JudgeOutputDto.class);
    }
}
