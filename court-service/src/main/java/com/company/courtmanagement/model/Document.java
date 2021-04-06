package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "documents")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documents_seq")
    @SequenceGenerator(name = "documents_seq", sequenceName = "documents_seq")
    @Column(name = "id")
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourtStage courtStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourtHearing courtHearing;

    private LocalDate creationDate;

    public Document(Long documentId) {
        this.documentId = documentId;
    }

    public Document() {
        // this constructor is used to initialize the object
    }

    public Optional<CourtStage> getOptionalCourtStage() {
        return Optional.ofNullable(courtStage);
    }

    public Optional<CourtHearing> getOptionalCourtHearing() {
        return Optional.ofNullable(courtHearing);
    }

    public void validate() {
        if (getOptionalCourtStage().flatMap(CourtStage::getOptionalId).isEmpty()
                && getOptionalCourtHearing().flatMap(CourtHearing::getOptionalId).isEmpty()) {
            throw new ResourceNotValidException("Document should belong to court stage or court hearing");
        }
    }
}
