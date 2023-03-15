package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.DtoMapper;
import com.funnco.petcheckserver.dto.request.RequestLocationDto;
import com.funnco.petcheckserver.dto.response.EmptyResponse;
import com.funnco.petcheckserver.dto.response.ResponseLocationDto;
import com.funnco.petcheckserver.entity.Location;
import com.funnco.petcheckserver.entity.VisitedLocation;
import com.funnco.petcheckserver.repository.AnimalRepository;
import com.funnco.petcheckserver.repository.LocationRepository;
import com.funnco.petcheckserver.repository.VisitedLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LocationController {

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final LocationRepository locationRepository;
    private final VisitedLocationRepository visitedLocationRepository;
    private final AnimalRepository animalRepository;

    public LocationController(LocationRepository locationRepository, VisitedLocationRepository visitedLocationRepository,
                              AnimalRepository animalRepository) {
        this.locationRepository = locationRepository;
        this.visitedLocationRepository = visitedLocationRepository;
        this.animalRepository = animalRepository;
    }

    @GetMapping("/locations/{pointId}")
    public ResponseEntity<ResponseLocationDto> getLocation(@PathVariable("pointId") Long id){
        if(id == null || id <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad pointId");
        }
        Location location = locationRepository.findById(id).orElse(null);
        if(location == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }
        return ResponseEntity.ok(DtoMapper.mapEntityLocationToResponseDto(location));
    }

    @PostMapping("/locations")
    public ResponseEntity<ResponseLocationDto> addLocation(@RequestBody RequestLocationDto body){
        Location newLocation = DtoMapper.mapRequestLocationDtoToEntity(body);
        if(newLocation.getLatitude() == null || newLocation.getLongitude() == null
                || newLocation.getLatitude() < -90 || newLocation.getLatitude() > 90
                || newLocation.getLongitude() < -180 || newLocation.getLongitude() > 180){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Got bad lat lng");
        }
        Location tempLocation = locationRepository.findLocationByLatitudeAndLongitude(newLocation.getLatitude(), newLocation.getLongitude()).orElse(null);
        if(tempLocation != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Location with such lat lng already exists with id " + tempLocation.getId());
        }

        locationRepository.save(newLocation);
        newLocation = locationRepository.findLocationByLatitudeAndLongitude(newLocation.getLatitude(), newLocation.getLongitude()).get();
        logger.info("Saving location " + newLocation) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.mapEntityLocationToResponseDto(newLocation));
    }

    @PutMapping("/locations/{pointId}")
    public ResponseEntity<ResponseLocationDto> changeLocation(@PathVariable("pointId") Long pointId, @RequestBody RequestLocationDto newLocation){
        if(pointId == null || pointId <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad pointId");
        }
        Location location = locationRepository.findById(pointId).orElse(null);
        if(location == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }
        if(newLocation.getLatitude() == null || newLocation.getLongitude() == null
                || newLocation.getLatitude() < -90 || newLocation.getLatitude() > 90
                || newLocation.getLongitude() < -180 || newLocation.getLongitude() > 180){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Got bad lat lng");
        }
        Location tempLocation = locationRepository.findLocationByLatitudeAndLongitude(newLocation.getLatitude(), newLocation.getLongitude()).orElse(null);
        if(tempLocation != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Location with such lat and lng already exists");
        }
        location.setLongitude(newLocation.getLongitude());
        location.setLatitude(newLocation.getLatitude());
        locationRepository.save(location);
        return ResponseEntity.ok(DtoMapper.mapEntityLocationToResponseDto(location));
    }

    @DeleteMapping("/locations/{pointId}")
    public ResponseEntity<EmptyResponse> deleteLocation(@PathVariable("pointId") Long pointId){
        if(pointId == null || pointId <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad pointId");
        }
        Location location = locationRepository.findById(pointId).orElse(null);
        if(location == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }
        VisitedLocation connectedLocation = visitedLocationRepository.findVisitedLocationByLocationPoint_Id(location.getId()).orElse(null);
        if(connectedLocation != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is linked to one of the visited locations");
        }

        if(animalRepository.existsAnimalByChippingLocation_Id(pointId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is linked to one of the chipping places");
        }

        locationRepository.delete(location);
        return ResponseEntity.ok(new EmptyResponse());
    }

}
