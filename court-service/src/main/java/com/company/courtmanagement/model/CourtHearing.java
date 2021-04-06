package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import lombok.Data;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Entity
@Table(name = "court_hearings")
@Data
public class CourtHearing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "court_hearings_seq")
    @SequenceGenerator(name = "court_hearings_seq", sequenceName = "court_hearings_seq")
    @Column(name = "id")
    private Long courtHearingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourtStage courtStage;

    @OneToMany(
            mappedBy = "courtHearing",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Collection<Document> documents = new ArrayList<>();

    private CourtHearingResult result;

    private LocalDate courtHearingDate;

    public CourtHearing(Long courtHearingId) {
        this.courtHearingId = courtHearingId;
    }

    public CourtHearing() {
        // this constructor is used to initialize the object
    }

    public enum CourtHearingResult {
        COURT_CASE_STARTED,
        POSTPONED,
        COURT_CASE_CLOSED
    }

    public Optional<Long> getOptionalId() {
        return Optional.ofNullable(courtHearingId);
    }

    private Optional<CourtStage> getOptionalCourtStage() {
        return Optional.ofNullable(courtStage);
    }

    public void validate() {
        this.getOptionalCourtStage().flatMap(CourtStage::getOptionalId).orElseThrow(()
                -> new ResourceNotValidException("Court hearing should contain court stage"));
    }
}
