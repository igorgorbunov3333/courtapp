package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class Address {
    private String street;
    private String building;
    private String region;
    private String city;

    public void validate() {
        if (StringUtils.isBlank(this.street)) {
            throw new ResourceNotValidException("Address should contain street");
        }
        if (StringUtils.isBlank(this.building)) {
            throw new ResourceNotValidException("Address should contain building");
        }
        if (StringUtils.isBlank(this.region)) {
            throw new ResourceNotValidException("Address should contain region");
        }
        if (StringUtils.isBlank(this.city)) {
            throw new ResourceNotValidException("Address should contain city");
        }
    }
}
