package com.company.courtmanagement.view.dto.output;

import com.company.courtmanagement.model.CourtHearing;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("CourtHearing")
public class CourtHearingOutputDto {
    private Long courtHearingId;
    private Long courtStage; // name must match with CourtHearing.courtStage field for correct ModelMapper work
    private Collection<DocumentOutputDto> documents = new ArrayList<>();
    private CourtHearing.CourtHearingResult result;
    private LocalDate courtHearingDate;
}
