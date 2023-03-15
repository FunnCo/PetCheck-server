package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.DtoMapper;
import com.funnco.petcheckserver.dto.request.RequestChangedVisitedLocationDto;
import com.funnco.petcheckserver.dto.response.EmptyResponse;
import com.funnco.petcheckserver.dto.response.ResponseVisitedLocationDto;
import com.funnco.petcheckserver.entity.Animal;
import com.funnco.petcheckserver.entity.LifeStatusEnum;
import com.funnco.petcheckserver.entity.Location;
import com.funnco.petcheckserver.entity.VisitedLocation;
import com.funnco.petcheckserver.dto.request.RequestVisitedLocationDto;
import com.funnco.petcheckserver.repository.AnimalRepository;
import com.funnco.petcheckserver.repository.LocationRepository;
import com.funnco.petcheckserver.repository.VisitedLocationRepository;
import com.funnco.petcheckserver.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class VisitedLocationController {

    private final VisitedLocationRepository visitedLocationRepository;
    private final AnimalRepository animalRepository;
    private final LocationRepository locationRepository;

    public VisitedLocationController(VisitedLocationRepository visitedLocationRepository, AnimalRepository animalRepository, LocationRepository locationRepository) {
        this.visitedLocationRepository = visitedLocationRepository;
        this.animalRepository = animalRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/animals/{animalId}/locations")
    public ResponseEntity<List<ResponseVisitedLocationDto>> getVisitedLocations(
            @PathVariable("animalId") Long animalId,
            @RequestParam(value = "startDateTime", required = false) String startDateTime,
            @RequestParam(value = "endDateTime", required = false) String endDateTime,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer searchFrom,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer sizeOfResponse
    ) {
        if (animalId == null || animalId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        if (searchFrom == null) {
            searchFrom = 0;
        }
        if (sizeOfResponse == null) {
            sizeOfResponse = 10;
        }
        if (searchFrom < 0 || sizeOfResponse <= 0
                || !DateUtils.isDateInISO8601(startDateTime)
                || !DateUtils.isDateInISO8601(endDateTime)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or many parameters are bad");
        }

        List<VisitedLocation> matchingResults = animal.getVisitedLocations();
        if (startDateTime != null) {
            matchingResults.removeIf(location ->
                    location.getDateTimeOfVisitLocationPoint().before(DateUtils.parseStringToDate(startDateTime)));
        }
        if (endDateTime != null) {
            matchingResults.removeIf(location ->
                    location.getDateTimeOfVisitLocationPoint().after(DateUtils.parseStringToDate(endDateTime)));
        }

        matchingResults.subList(0, searchFrom).clear();
        List<ResponseVisitedLocationDto> responseList = new ArrayList<>();
        for (int i = 0; i < sizeOfResponse && i < matchingResults.size(); i++) {
            responseList.add(DtoMapper.mapEntityVisitedLocationToDto(matchingResults.get(i)));
        }
        responseList = responseList.stream().sorted(Comparator.comparing(ResponseVisitedLocationDto::getDateTimeOfVisitLocationPoint)).toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/animals/{animalId}/locations/{pointId}")
    @Transactional
    public ResponseEntity<ResponseVisitedLocationDto> createVisitedLocation(
            @PathVariable("animalId") Long animalId,
            @PathVariable("pointId") Long pointId
    ) {

        if ((animalId != null && animalId <= 0) || (pointId != null && pointId <= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        Location location = locationRepository.findById(pointId).orElse(null);
        if (location == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }

        if (animal.getLifeStatus() == LifeStatusEnum.DEAD) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal is dead");
        }

        if (location.getLatitude().equals(animal.getChippingLocation().getLatitude())
                && location.getLongitude().equals(animal.getChippingLocation().getLongitude())
                && animal.getVisitedLocations().size() == 0
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal is at chipping place");
        }

        if (animal.getVisitedLocations().size() != 0) {
            Location lastPlace = animal.getVisitedLocations().get(animal.getVisitedLocations().size() - 1).getLocationPoint();
            if (Objects.equals(lastPlace.getLatitude(), location.getLatitude())
                    && Objects.equals(lastPlace.getLongitude(), location.getLongitude())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal didn't move");
            }
        }

        VisitedLocation newVisitedLocation = new VisitedLocation();
        Date dateOfAdding = new Date();
        newVisitedLocation.setLocationPoint(location);
        newVisitedLocation.setDateTimeOfVisitLocationPoint(dateOfAdding);
        newVisitedLocation = visitedLocationRepository.saveAndFlush(newVisitedLocation);

        animal.getVisitedLocations().add(newVisitedLocation);
        animalRepository.saveAndFlush(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.mapEntityVisitedLocationToDto(newVisitedLocation));
    }

    @PutMapping("/animals/{animalId}/locations")
    @Transactional
    public ResponseEntity<ResponseVisitedLocationDto> changeVisitedLocation(
            @PathVariable("animalId") Long animalId,
            @RequestBody RequestChangedVisitedLocationDto changedLocation
    ) {
        if (animalId != null && animalId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        if (changedLocation.getLocationPointId() == null
                || changedLocation.getLocationPointId() <= 0
                || changedLocation.getVisitedLocationPointId() == null
                || changedLocation.getVisitedLocationPointId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad location id");
        }

        Location newLocation = locationRepository.findById(changedLocation.getLocationPointId()).orElse(null);
        if (newLocation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No point with such id found");
        }

        VisitedLocation changedVisitedLocation = visitedLocationRepository.findById(changedLocation.getVisitedLocationPointId()).orElse(null);
        if (changedVisitedLocation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No visited point with such id found");
        }

        if (animal.getVisitedLocations()
                .stream()
                .noneMatch(visitedLocation -> visitedLocation.getId().equals(changedVisitedLocation.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such visited point with this animal found");
        }

        List<VisitedLocation> allPreviousLocations = new ArrayList<>(animal.
                getVisitedLocations()
                .stream()
                .sorted(Comparator.comparing(VisitedLocation::getDateTimeOfVisitLocationPoint))
                .toList());

        if (changedLocation.getVisitedLocationPointId().equals(allPreviousLocations.get(0).getId())
                && changedLocation.getLocationPointId().equals(animal.getChippingLocation().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change first visited location on chipping location");
        }

        int indexOfChangedLocation = -1;
        for (int i = 0; i < allPreviousLocations.size(); i++) {
            if (allPreviousLocations.get(i).getId().equals(changedLocation.getVisitedLocationPointId())) {
                indexOfChangedLocation = i;
                break;
            }
        }

        if (allPreviousLocations.get(indexOfChangedLocation).getLocationPoint().getId().equals(changedLocation.getLocationPointId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change location on the same/next/previous location");
        }
        if (indexOfChangedLocation < allPreviousLocations.size() - 1
                && allPreviousLocations.get(indexOfChangedLocation + 1).getLocationPoint().getId().equals(changedLocation.getLocationPointId())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change location on the same/next/previous location");
        }
        if (indexOfChangedLocation != 0
                && allPreviousLocations.get(indexOfChangedLocation - 1).getLocationPoint().getId().equals(changedLocation.getLocationPointId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change location on the same/next/previous location");
        }

        changedVisitedLocation.setLocationPoint(newLocation);
        visitedLocationRepository.saveAndFlush(changedVisitedLocation);
        return ResponseEntity.ok(DtoMapper.mapEntityVisitedLocationToDto(changedVisitedLocation));
    }

    @DeleteMapping("/animals/{animalId}/locations/{visitedPointId}")
    @Transactional
    public ResponseEntity<EmptyResponse> deleteVisitedLocation(
            @PathVariable("animalId") Long animalId,
            @PathVariable("visitedPointId") Long pointId
    ) {
        if (animalId == null || animalId <= 0 || pointId == null || pointId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }
        VisitedLocation locationToDelete = visitedLocationRepository.findById(pointId).orElse(null);
        if (locationToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Visited location with such id is not found");
        }
        if (animal.getVisitedLocations()
                .stream()
                .noneMatch(visitedLocation -> visitedLocation.getId().equals(locationToDelete.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such visited point with this animal found");
        }

        List<VisitedLocation> allVisitedLocations = new ArrayList<>(animal.
                getVisitedLocations()
                .stream()
                .sorted(Comparator.comparing(VisitedLocation::getDateTimeOfVisitLocationPoint))
                .toList());

        allVisitedLocations.removeIf(location -> location.getId().equals(locationToDelete.getId()));

        if(allVisitedLocations.size() > 0) {
            if (animal.getChippingLocation().getId().equals(allVisitedLocations.get(0).getLocationPoint().getId())) {
                allVisitedLocations.remove(0);
            }
        }

        animal.setVisitedLocations(allVisitedLocations);
        animalRepository.save(animal);

        visitedLocationRepository.delete(locationToDelete);
        return ResponseEntity.ok(new EmptyResponse());
    }
}
