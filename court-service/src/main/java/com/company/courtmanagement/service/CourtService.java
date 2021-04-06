package com.company.courtmanagement.service;

import com.company.courtmanagement.model.Court;

import java.util.Collection;

public interface CourtService {

    Court findCourt(long courtId);

    Collection<Court> findAllCourts();

    Court findCourtById(long courtId);

    Court saveCourt(Court court);
}
