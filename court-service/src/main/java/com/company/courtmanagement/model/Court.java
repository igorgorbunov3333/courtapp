package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Entity
@Table(name = "courts")
@Data
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courts_seq")
    @SequenceGenerator(name = "courts_seq", sequenceName = "courts_seq")
    @Column(name = "id")
    private Long courtId;
    private String name;
    @ToString.Exclude
    private Address address;
    private Proceeding proceeding;

    @OneToMany(
            mappedBy = "court",
            cascade = CascadeType.ALL
    )
    private Collection<Judge> judges = new ArrayList<>();

    public Court(Long courtId) {
        this.courtId = courtId;
    }

    public Court() {
        // this constructor is used to initialize the object
    }

    public enum Proceeding {
        COMMON, ECONOMIC, ADMINISTRATIVE, CONSTITUTIONAL
    }

    public Optional<Long> getOptionalId() {
        return Optional.ofNullable(courtId);
    }

    public void addJudge(Judge judge) {
        judge.setCourt(this);
        this.judges.add(judge);
    }

    public void validate() {
        if (this.address == null) {
            throw new ResourceNotValidException("Court should contain address");
        }
        this.address.validate();
        if (StringUtils.isBlank(this.name)) {
            throw new ResourceNotValidException("Court should contain name");
        }
        if (this.proceeding == null) {
            throw new ResourceNotValidException("Court should contain proceeding");
        }
    }
}
