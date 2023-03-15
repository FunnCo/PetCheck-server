package com.funnco.petcheckserver.repository;

import com.funnco.petcheckserver.entity.Location;
import com.funnco.petcheckserver.entity.VisitedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, Long> {

    VisitedLocation findVisitedLocationByDateTimeOfVisitLocationPoint(Date date);
    List<VisitedLocation> findVisitedLocationsByDateTimeOfVisitLocationPointAfter(Date date);
    List<VisitedLocation> findVisitedLocationsByDateTimeOfVisitLocationPointBefore(Date date);

    Optional<VisitedLocation> findVisitedLocationByLocationPoint_Id(Long id);
    Optional<List<VisitedLocation>> findVisitedLocationsByLocationPoint_Id(Long id);
}
