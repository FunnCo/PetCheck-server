package com.funnco.petcheckserver.repository;

import com.funnco.petcheckserver.entity.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findLocationByLatitudeAndLongitude(Double lat, Double lng);
}
