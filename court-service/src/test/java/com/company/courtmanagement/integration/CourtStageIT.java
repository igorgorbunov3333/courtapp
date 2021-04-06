package com.company.courtmanagement.integration;

import com.company.courtmanagement.TestPostgreSQLContainer;
import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.repository.CourtCaseRepository;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.repository.CourtStageRepository;
import com.company.courtmanagement.repository.DocumentRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourtStageIT {
    @Autowired
    private CourtCaseRepository courtCaseRepository;
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private CourtStageRepository stageRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @ClassRule
    public static TestPostgreSQLContainer testPostgreSQLContainer = TestPostgreSQLContainer.getInstance();

    @Test
    @Transactional
    public void testFindCourtStagesByCourtCase_CourtCaseId_whenFind_thenReturnSortedCourtStages() {
        CourtCase courtCase = new CourtCase();
        courtCase.setAccountId(1L);
        CourtCase savedCase = courtCaseRepository.save(courtCase);

        Court court = buildCourt();
        Court savedCourt = courtRepository.save(court);

        LocalDate firstDate = LocalDate.of(2020, 1, 1);
        CourtStage firstCourtStage =
                buildStage(savedCase, savedCourt, firstDate, LocalDate.of(2020, 1, 28));
        stageRepository.save(firstCourtStage);

        LocalDate thirdDate = LocalDate.of(2020, 3, 1);
        CourtStage thirdCourtStage =
                buildStage(courtCase, savedCourt, thirdDate, LocalDate.of(2020, 3, 28));
        stageRepository.save(thirdCourtStage);

        LocalDate secondDate = LocalDate.of(2020, 2, 1);
        CourtStage secondCourtStage =
                buildStage(courtCase, savedCourt, secondDate, LocalDate.of(2020, 2, 28));
        stageRepository.save(secondCourtStage);

        Collection<CourtStage> stages =
                stageRepository.findByCourtCase_CourtCaseIdOrderByStart(savedCase.getCourtCaseId());
        Iterator<CourtStage> stageIterator = stages.iterator();

        CourtStage first = stageIterator.next();
        CourtStage second = stageIterator.next();
        CourtStage third = stageIterator.next();

        assertEquals(firstDate, first.getStart());
        assertEquals(secondDate, second.getStart());
        assertEquals(thirdDate, third.getStart());
    }

    @Test
    @Transactional
    public void testSaveCourtStage_whenFindById_thenReturnSortedDocuments() {
        CourtCase courtCase = new CourtCase();
        courtCase.setAccountId(1L);
        CourtCase savedCourtCase = courtCaseRepository.save(courtCase);

        Court court = buildCourt();
        Court savedCourt = courtRepository.save(court);

        CourtStage courtStage = new CourtStage();
        courtStage.setCourtCase(savedCourtCase);

        Document document1 = new Document();
        document1.setCreationDate(LocalDate.of(2020, 4, 1));
        Document document2 = new Document();
        document2.setCreationDate(LocalDate.of(2020, 5, 1));

        courtStage.addDocument(document2);
        courtStage.addDocument(document1);
        courtStage.setCourt(savedCourt);
        LocalDate firstDate = LocalDate.of(2020, 1, 1);
        courtStage.setStart(firstDate);
        courtStage.setStop(LocalDate.of(2020, 1, 28));
        CourtStage savedCourtStage = stageRepository.save(courtStage);

        CourtStage actualCourtStage = stageRepository.getOne(savedCourtStage.getCourtStageId());

        Collection<Document> documents = actualCourtStage.getSortedDocuments();
        Iterator<Document> documentIterator = documents.iterator();

        Document actualFirstDocument = documentIterator.next();
        Document actualSecondDocument = documentIterator.next();

        assertTrue(actualFirstDocument.getCreationDate().isBefore(actualSecondDocument.getCreationDate()));
    }

    private CourtStage buildStage(CourtCase savedCourtCase, Court savedCourt,
                                  LocalDate startDate, LocalDate stopDate) {
        CourtStage courtStage = new CourtStage();
        courtStage.setCourtCase(savedCourtCase);
        Document document = new Document();
        document.setCreationDate(LocalDate.now());
        courtStage.addDocument(document);
        courtStage.setCourt(savedCourt);
        courtStage.setStart(startDate);
        courtStage.setStop(stopDate);
        return courtStage;
    }

    private Court buildCourt() {
        Court court = new Court();
        court.setName("name");

        Address address = new Address();
        address.setStreet("street");
        address.setRegion("region");
        address.setCity("city");
        address.setBuilding("building");
        court.setAddress(address);

        court.setProceeding(Court.Proceeding.COMMON);
        return court;
    }
}
