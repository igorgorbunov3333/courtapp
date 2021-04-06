package com.company.courtmanagement.model;

import lombok.Data;
import lombok.ToString;

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

@Data
@Entity
@Table(name = "court_cases")
public class CourtCase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "court_cases_seq")
    @SequenceGenerator(name = "court_cases_seq", sequenceName = "court_cases_seq")
    @Column(name = "id")
    private Long courtCaseId;

    @Column(name = "account_id")
    private Long accountId;

    @OneToMany(
            mappedBy = "courtCase",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Collection<CourtStage> courtStages = new ArrayList<>();

    public CourtCase(Long courtCaseId) {
        this.courtCaseId = courtCaseId;
    }

    public CourtCase() {
        // this constructor is used to initialize the object
    }

    public Optional<Long> getOptionalId() {
        return Optional.ofNullable(courtCaseId);
    }

    public Optional<Long> getOptionalAccountId() {
        return Optional.ofNullable(accountId);
    }

    public void addCourtStage(CourtStage courtStage) {
        courtStages.add(courtStage);
        courtStage.setCourtCase(this);
    }
}
