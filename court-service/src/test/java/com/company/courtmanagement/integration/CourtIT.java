package com.company.courtmanagement.integration;

import com.company.courtmanagement.TestPostgreSQLContainer;
import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.model.Judge;
import com.company.courtmanagement.service.CourtService;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourtIT {
    @Autowired
    private CourtService courtService;
    @ClassRule
    public static TestPostgreSQLContainer testPostgreSQLContainer = TestPostgreSQLContainer.getInstance();

    @Test
    @Transactional
    public void testSaveCourt_whenSaveSuccessfully_thenWithJudges() {
        Court court = buildCourt();
        court.addJudge(buildJudge());
        Long savedCourtId = courtService.saveCourt(court).getCourtId();
        Court savedCourt = courtService.findCourtById(savedCourtId);
        assertThat(savedCourt.getJudges(), is(not(empty())));
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
