package com.company.courtmanagement.view.dto.output;

import com.company.courtmanagement.model.CourtStage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("CourtStage")
public class CourtStageOutputDto {
    private Long courtStageId;
    private CourtStage.StageResult stageResult;
    private CourtOutputDto court;
    private Long courtCase; // name must match with CourtStage.courtCase field for correct ModelMapper work
    private Collection<DocumentOutputDto> documents = new ArrayList<>();
    private Collection<CourtHearingOutputDto> courtHearings = new ArrayList<>();

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate start;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate stop;
}
