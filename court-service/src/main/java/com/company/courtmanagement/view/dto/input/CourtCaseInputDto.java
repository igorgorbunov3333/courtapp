package com.company.courtmanagement.view.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("CourtCase")
public class CourtCaseInputDto {
    @Positive
    @NotNull
    private Long accountId;
    private Collection<CourtStageInputDto> courtStages = new ArrayList<>();
}
