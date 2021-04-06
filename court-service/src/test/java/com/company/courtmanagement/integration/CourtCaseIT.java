package com.company.courtmanagement.integration;

import com.company.courtmanagement.TestPostgreSQLContainer;
import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.CourtCase;
import com.company.courtmanagement.model.CourtStage;
import com.company.courtmanagement.model.Document;
import com.company.courtmanagement.service.CourtCaseService;
import com.company.courtmanagement.service.CourtService;
import com.company.courtmanagement.service.CourtStageService;
import com.company.courtmanagement.service.DocumentService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourtCaseIT {
    @Autowired
    private CourtService courtService;
    @Autowired
    private CourtCaseService courtCaseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private CourtStageService courtStageService;
    @ClassRule
    public static TestPostgreSQLContainer testPostgreSQLContainer = TestPostgreSQLContainer.getInstance();

    @Test
    @Transactional
    public void testDeleteCourtCase_whenDeleteSuccessfully_thenDeleteMappedEntities() {
        CourtCase savedCourtCase = courtCaseService.saveCourtCase(buildManagedCourtCase());
        CourtStage savedStage = savedCourtCase.getCourtStages().iterator().next();

        Long firstCourtStageId = savedStage.getCourtStageId();
        Long firstDocumentId = savedStage.getDocuments().iterator().next().getDocumentId();
        Long courtId = savedStage.getCourt().getCourtId();

        courtCaseService.deleteCourtCase(savedCourtCase.getCourtCaseId());

        try {
            CourtStage courtStage = courtStageService.findCourtStage(firstCourtStageId);
            assertNull(courtStage);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }

        try {
            Document document = documentService.findDocument(firstDocumentId);
            assertNull(document);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }

        Court court = courtService.findCourt(courtId);
        assertNotNull(court);
    }

    @Test
    @Transactional
    public void testCourtCaseMapping_whenSave_thenUpdateCourtStageWithNewDocument() {
        Court savedCourt = courtService.saveCourt(buildCourt());
        CourtStage stage = new CourtStage();
        stage.setCourt(savedCourt);
        stage.setStart(LocalDate.now());

        Document document = new Document();
        document.setCreationDate(LocalDate.now());
        stage.addDocument(document);

        CourtCase courtCase = new CourtCase();
        courtCase.setAccountId(1L);
        courtCase.addCourtStage(stage);

        CourtCase savedCase = courtCaseService.saveCourtCase(courtCase);

        CourtCase actualCourtCase = courtCaseService.findCourtCase(savedCase.getCourtCaseId());
        assertThat(actualCourtCase.getCourtStages(), is(not(empty())));
        Collection<Document> documents = actualCourtCase.getCourtStages().iterator().next().getDocuments();
        assertThat(documents, is(not(empty())));
        Document actualDocument = documents.iterator().next();
        assertNotNull(actualDocument.getCourtStage());
        assertNotNull(actualDocument.getCourtStage().getCourtCase());
    }

    private CourtCase buildManagedCourtCase() {
        Court court = buildCourt();
        courtService.saveCourt(court);

        CourtCase courtCase = new CourtCase();

        CourtStage firstCourtStage = new CourtStage();
        firstCourtStage.setCourtCase(courtCase);
        Document document = new Document();
        document.setCreationDate(LocalDate.now());
        firstCourtStage.addDocument(document);
        firstCourtStage.setCourt(court);
        firstCourtStage.setStart(LocalDate.of(2020, 3, 1));
        firstCourtStage.setStop(LocalDate.of(2020, 3, 28));

        courtCase.addCourtStage(firstCourtStage);
        courtCase.setAccountId(1L);
        return courtCase;
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
