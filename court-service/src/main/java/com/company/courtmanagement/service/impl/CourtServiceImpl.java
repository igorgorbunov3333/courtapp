package com.company.courtmanagement.service.impl;

import com.company.courtmanagement.model.Court;
import com.company.courtmanagement.repository.CourtRepository;
import com.company.courtmanagement.service.CourtService;
import com.company.courtmanagement.service.exception.ResourceNotFoundException;
import com.company.courtmanagement.service.exception.ResourceNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CourtServiceImpl implements CourtService {
    private CourtRepository courtRepository;

    @Autowired
    public CourtServiceImpl(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Override
    public Court findCourt(long courtId) {
        Optional<Court> courtOptional = courtRepository.findById(courtId);
        return courtOptional.orElseThrow(()
                -> new ResourceNotFoundException("No court with id: " + courtId));
    }

    @Override
    public Collection<Court> findAllCourts() {
        return courtRepository.findAll();
    }

    @Override
    public Court findCourtById(long courtId) {
        return courtRepository.getOne(courtId);
    }

    @Override
    public Court saveCourt(Court court) {
        if (court == null) {
            throw new ResourceNotValidException("Court should be not null");
        }
        court.validate();
        return courtRepository.save(court);
    }
}
