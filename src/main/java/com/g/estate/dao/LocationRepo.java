package com.g.estate.dao;

import com.g.estate.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {
    List<Location> getLocationsByType(String type);

    Location getLocationsById(String id);

}
