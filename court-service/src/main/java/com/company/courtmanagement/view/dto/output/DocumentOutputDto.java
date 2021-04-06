package com.company.courtmanagement.view.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Document")
public class DocumentOutputDto {
    private Long documentId;
    private Long courtStage; // name must match with Document.courtStage field for correct ModelMapper work
    private Long courtHearing; // name must match with Document.courtHearing field for correct ModelMapper work
}
