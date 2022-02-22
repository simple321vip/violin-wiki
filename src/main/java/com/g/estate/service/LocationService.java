package com.g.estate.service;

import com.g.estate.dao.LocationRepo;
import com.g.estate.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;


@Service
public class LocationService {

    @Autowired
    private LocationRepo locationRepo;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public List<Location> get() {
        return locationRepo.getLocationsByType("1");
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Location> getAll() {
        return this.get();
    }

}
