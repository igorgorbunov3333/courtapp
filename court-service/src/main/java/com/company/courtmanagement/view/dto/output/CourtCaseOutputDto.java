package com.company.courtmanagement.view.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("CourtCase")
public class CourtCaseOutputDto {
    private Long courtCaseId;
    private Long accountId;
    private Collection<CourtStageOutputDto> courtStages = new ArrayList<>();
}
