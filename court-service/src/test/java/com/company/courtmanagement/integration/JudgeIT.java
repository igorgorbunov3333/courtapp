package com.company.courtmanagement.integration;

import com.company.courtmanagement.TestPostgreSQLContainer;
import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.service.CourtService;
import com.company.courtmanagement.service.JudgeService;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JudgeIT {
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private CourtService courtService;
    @ClassRule
    public static TestPostgreSQLContainer testPostgreSQLContainer = TestPostgreSQLContainer.getInstance();

    @Test
    @Transactional
    public void testSaveJudge_ShouldUpdateCourt_WhenSaveJudgeWithPersistedCourtField() {
        Judge judge = buildJudge();

        Court court = buildCourt();
        courtService.saveCourt(court);

        judge.setCourt(court);

        Long savedJudgeId = judgeService.saveJudge(judge).getJudgeId();
        Judge savedJudge = judgeService.findJudgeById(savedJudgeId);
        assertNotNull(courtService.findCourtById(savedJudge.getCourt().getCourtId()));
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

    private Judge buildJudge() {
        return new Judge("Firstname", "surname", "patronimic");
    }
}
