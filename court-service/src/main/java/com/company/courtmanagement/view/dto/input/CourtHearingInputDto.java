package com.company.courtmanagement.view.dto.input;

import com.company.courtmanagement.model.CourtHearing;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ApiModel("CourtHearing")
public class CourtHearingInputDto {
    @NotNull
    @Positive
    private Long courtStage; // name must match with Document.courtStage field for correct ModelMapper work
    @NotEmpty
    private Collection<DocumentInputDto> documents = new ArrayList<>();
    private CourtHearing.CourtHearingResult result;
}
