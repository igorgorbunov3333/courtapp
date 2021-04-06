package com.company.courtmanagement.view.dto.input;

import com.company.courtmanagement.model.CourtStage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("CourtStage")
public class CourtStageInputDto {
    private Long courtStageId;
    private CourtStage.StageResult stageResult;
    @NotNull
    @Positive
    private Long court; // name must match with CourtStage.court field for correct ModelMapper work
    @NotNull
    @Positive
    private Long courtCase; // name must match with CourtStage.court field for correct ModelMapper work
    private Collection<DocumentInputDto> documents = new ArrayList<>();
    private Collection<CourtHearingInputDto> courtHearings = new ArrayList<>();

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate start;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate stop;
}


