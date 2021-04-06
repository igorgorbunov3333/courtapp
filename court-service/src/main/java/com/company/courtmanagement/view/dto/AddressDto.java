package com.company.courtmanagement.view.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("Address")
public class AddressDto {
    @NotNull
    @NotBlank
    private String street;
    @NotNull
    @NotBlank
    private String building;
    @NotNull
    @NotBlank
    private String region;
    @NotNull
    @NotBlank
    private String city;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
