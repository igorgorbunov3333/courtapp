package com.company.courtmanagement.view.dto.output;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.view.dto.AddressDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Court")
public class CourtOutputDto {
    private Long courtId;
    private String name;
    private AddressDto address;
    private Court.Proceeding proceeding;
}
