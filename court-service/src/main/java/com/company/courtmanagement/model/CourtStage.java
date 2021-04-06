package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.util.ErrorMsgHelper;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

@Entity
@Table(name = "court_stages")
@Data
public class CourtStage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "court_stages_seq")
    @SequenceGenerator(name = "court_stages_seq", sequenceName = "court_stages_seq")
    @Column(name = "id")
    private Long courtStageId;
    @Column(name = "stage_result")
    private StageResult stageResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Court court;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourtCase courtCase;

    @OneToMany(
            mappedBy = "courtStage",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Collection<Document> documents = new ArrayList<>();

    @OneToMany(
            mappedBy = "courtStage",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Collection<CourtHearing> courtHearings = new ArrayList<>();

    private LocalDate start;
    private LocalDate stop;

    public CourtStage(Long courtStageId) {
        this.courtStageId = courtStageId;
    }

    public CourtStage() {
        // this constructor is used to initialize the object
    }

    public enum StageResult {
        WIN,
        LOST,
        CLOSED
    }

    public Optional<Long> getOptionalId() {
        return Optional.ofNullable(courtStageId);
    }

    public Optional<Court> getOptionalCourt() {
        return Optional.ofNullable(court);
    }

    public Optional<CourtCase> getOptionalCourtCase() {
        return Optional.ofNullable(courtCase);
    }

    public Collection<Document> getSortedDocuments() {
        if (!CollectionUtils.isEmpty(this.documents)) {
            Comparator<Document> comparator = (d1, d2) -> {
                if (d1.getCreationDate() == null || d2.getCreationDate() == null) {
                    throw new ResourceNotValidException("Document should contain creation date");
                }
                if (!d1.getCreationDate().equals(d2.getCreationDate())) {
                    return d1.getCreationDate().isBefore(d2.getCreationDate()) ? -1 : 1;
                }
                return 0;
            };
            Collection<Document> sortedDocuments = new TreeSet<>(comparator);
            sortedDocuments.addAll(documents);
            return sortedDocuments;
        }
        return Collections.emptyList();
    }

    public void addDocument(Document document) {
        documents.add(document);
        document.setCourtStage(this);
    }

    public void validate() {
        if (CollectionUtils.isEmpty(this.getDocuments())) {
            throw new ResourceNotValidException(ErrorMsgHelper.STAGE_NO_DOC);
        }
        this.getOptionalCourt().flatMap(Court::getOptionalId).orElseThrow(()
                -> new ResourceNotValidException(ErrorMsgHelper.STAGE_NO_COURT));
        this.getOptionalCourtCase().flatMap(CourtCase::getOptionalId).orElseThrow(()
                -> new ResourceNotValidException("Court stage should contain court case"));
    }

    public void setCreationDateForDocuments() {
        this.documents.forEach(d -> d.setCreationDate(LocalDate.now()));
    }
}


