package com.company.courtmanagement.view.dto.input;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.view.dto.AddressDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Court")
public class CourtInputDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private AddressDto address;
    @NotNull
    private Court.Proceeding proceeding;
}
