package com.company.courtmanagement.view.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Judge")
public class JudgeOutputDto {
    private Long judgeId;
    private String firstName;
    private String surname;
    private String patronymic;
    private CourtOutputDto court; // name must match with Judge.court field for correct ModelMapper work
}
