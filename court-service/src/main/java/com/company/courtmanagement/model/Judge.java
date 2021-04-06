package com.company.courtmanagement.model;

import com.company.courtmanagement.service.exception.ResourceNotValidException;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "judges")
@Data
public class Judge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "judges_seq")
    @SequenceGenerator(name = "judges_seq", sequenceName = "judges_seq")
    @Column(name = "id")
    private Long judgeId;

    @Column(name = "first_name")
    private String firstName;
    private String surname;
    private String patronymic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Court court;

    public Judge(String firstName, String surname, String patronymic) {
        this.firstName = firstName;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public Judge() {
        // this constructor is used to initialize the object
    }

    public Optional<Court> getOptionalCourt() {
        return Optional.ofNullable(court);
    }

    public void validate() {
        if (StringUtils.isBlank(this.firstName)) {
            throw new ResourceNotValidException("Judge should contain firstName");
        }
        if (StringUtils.isBlank(this.surname)) {
            throw new ResourceNotValidException("Judge should contain surname");
        }
        if (StringUtils.isBlank(this.patronymic)) {
            throw new ResourceNotValidException("Judge should contain patronymic");
        }
        getOptionalCourt().flatMap(Court::getOptionalId).orElseThrow(()
                -> new ResourceNotValidException("Judge should contain court"));
    }
}
