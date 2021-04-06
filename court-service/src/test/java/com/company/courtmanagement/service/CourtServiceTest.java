package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Address;
import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import com.company.courtmanagement.service.impl.CourtServiceImpl;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class CourtServiceTest {
    private CourtRepository courtRepository = mock(CourtRepository.class);
    private CourtService courtService = new CourtServiceImpl(courtRepository);

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenCourtNull_thenFail() {
        courtService.saveCourt(null);
        verifyZeroInteractions(courtRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenNoAddress_thenFail() {
        Court court = new Court();
        court.setName("name");
        court.setProceeding(Court.Proceeding.COMMON);
        courtService.saveCourt(court);
        verifyZeroInteractions(courtRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenNoAddress_Street_thenFail() {
        Court court = new Court();
        court.setName("name");
        court.setProceeding(Court.Proceeding.COMMON);

        Address address = new Address();
        address.setBuilding("building");
        address.setCity("city");
        address.setRegion("region");
        court.setAddress(address);
        courtService.saveCourt(court);
        verifyZeroInteractions(courtRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenNameNull_thenFail() {
        Court court = new Court();
        court.setProceeding(Court.Proceeding.COMMON);

        Address address = new Address();
        address.setBuilding("building");
        address.setCity("city");
        address.setRegion("region");
        address.setStreet("street");
        court.setAddress(address);
        courtService.saveCourt(court);
        verifyZeroInteractions(courtRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenNameBlank_thenFail() {
        Court court = new Court();
        court.setName("   ");
        court.setProceeding(Court.Proceeding.COMMON);

        Address address = new Address();
        address.setBuilding("building");
        address.setCity("city");
        address.setRegion("region");
        address.setStreet("street");
        court.setAddress(address);
        courtService.saveCourt(court);
        verifyZeroInteractions(courtRepository);
    }

    @Test(expected = ResourceNotValidException.class)
    public void testSaveCourt_whenNoProceeding_thenFail() {
        Court court = new Court();
        court.setName("name");

        Address address = new Address();
        address.setBuilding("building");
        address.setCity("city");
        address.setRegion("region");
        address.setStreet("street");
        court.setAddress(address);
        courtService.saveCourt(court);
        verifyZeroInteractions(courtRepository);
    }

    @Test
    public void testSaveCourt_whenValid_thenSuccessfullySave() {
        Court court = new Court();
        court.setName("name");
        court.setProceeding(Court.Proceeding.COMMON);

        Address address = new Address();
        address.setBuilding("building");
        address.setCity("city");
        address.setRegion("region");
        address.setStreet("street");
        court.setAddress(address);
        courtService.saveCourt(court);
        verify(courtRepository).save(court);
    }
}
