package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.DtoMapper;
import com.funnco.petcheckserver.dto.request.ChangeRequestAnimalDto;
import com.funnco.petcheckserver.dto.request.ChangeRequestAnimalTypeDto;
import com.funnco.petcheckserver.dto.request.CreateRequestAnimalDto;
import com.funnco.petcheckserver.dto.response.EmptyResponse;
import com.funnco.petcheckserver.dto.response.ResponseAnimalDto;
import com.funnco.petcheckserver.entity.*;
import com.funnco.petcheckserver.repository.AnimalRepository;
import com.funnco.petcheckserver.repository.AnimalTypeRepository;
import com.funnco.petcheckserver.repository.LocationRepository;
import com.funnco.petcheckserver.repository.UserRepository;
import com.funnco.petcheckserver.utils.DateUtils;
import com.funnco.petcheckserver.utils.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public AnimalController(AnimalRepository animalRepository,
                            AnimalTypeRepository animalTypeRepository,
                            UserRepository userRepository,
                            LocationRepository locationRepository) {
        this.animalRepository = animalRepository;
        this.animalTypeRepository = animalTypeRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/animals/{animalId}")
    public ResponseEntity<ResponseAnimalDto> getAnimal(@PathVariable("animalId") Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        Animal animal = animalRepository.findById(id).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }
        animal.setVisitedLocations(animal.getVisitedLocations().stream().sorted(Comparator.comparing(VisitedLocation::getDateTimeOfVisitLocationPoint)).toList());
        return ResponseEntity.ok(DtoMapper.mapEntityAnimalToDto(animal));
    }

    @GetMapping("/animals/search")
    public ResponseEntity<List<ResponseAnimalDto>> searchForAccounts(
            @RequestParam(value = "startDateTime", required = false) String startDateTime,
            @RequestParam(value = "endDateTime", required = false) String endDateTime,
            @RequestParam(value = "chipperId", required = false) Integer chipperId,
            @RequestParam(value = "chippingLocationId", required = false) Long chippingLocationId,
            @RequestParam(value = "lifeStatus", required = false) String lifeStatus,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer searchFrom,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer sizeOfResponse
    ) {
        if (searchFrom == null) {
            searchFrom = 0;
        }
        if (sizeOfResponse == null) {
            sizeOfResponse = 10;
        }
        if (searchFrom < 0 || sizeOfResponse <= 0
                || !DateUtils.isDateInISO8601(startDateTime)
                || !DateUtils.isDateInISO8601(endDateTime)
                || (chipperId != null && chipperId <= 0)
                || (chippingLocationId != null && chippingLocationId <= 0)
                || !EnumUtils.doesLifeStatusEnumContainValue(lifeStatus)
                || !EnumUtils.doesGenderEnumContainValue(gender)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or many parameters are bad");
        }

        List<Animal> matchingAnimals = new ArrayList<>();
        for(Animal existingAnimal : animalRepository.findAll()){
            matchingAnimals.add(existingAnimal);
        }

        if(chipperId != null){
            matchingAnimals.removeIf(animal -> !animal.getChipper().getId().equals(chipperId));
        }
        if(chippingLocationId != null){
            matchingAnimals.removeIf(animal -> !animal.getChippingLocation().getId().equals(chippingLocationId));
        }
        if(startDateTime != null){
            matchingAnimals.removeIf(animal -> !animal.getChippingDateTime().before(DateUtils.parseStringToDate(startDateTime)));
        }
        if(endDateTime != null){
            matchingAnimals.removeIf(animal -> !animal.getChippingDateTime().after(DateUtils.parseStringToDate(endDateTime)));
        }
        if(gender != null){
            matchingAnimals.removeIf(animal -> animal.getGender() != GenderEnum.valueOf(gender));
        }
        if(lifeStatus != null){
            matchingAnimals.removeIf(animal -> animal.getLifeStatus() != LifeStatusEnum.valueOf(lifeStatus));
        }

        matchingAnimals = new ArrayList<>(matchingAnimals
                .stream()
                .collect(Collectors.toMap(Animal::getId, animal -> animal, (value1, value2) -> value1))
                .values()
                .stream()
                .sorted(Comparator.comparing(Animal::getId))
                .toList());

        matchingAnimals.subList(0, searchFrom).clear();

        List<ResponseAnimalDto> responseList = new ArrayList<>();
        for (int i = 0; i < sizeOfResponse && i < matchingAnimals.size(); i++) {
            responseList.add(DtoMapper.mapEntityAnimalToDto(matchingAnimals.get(i)));
        }

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/animals")
    public ResponseEntity<ResponseAnimalDto> addAnimal(@RequestBody CreateRequestAnimalDto request) {
        if ((
                request.getChipperId() == null || request.getChipperId() <= 0
                        || request.getChippingLocationId() == null || request.getChippingLocationId() <= 0
                        || request.getGender() == null || !EnumUtils.doesGenderEnumContainValue(request.getGender())
                        || request.getHeight() == null || request.getHeight() <= 0
                        || request.getWeight() == null || request.getWeight() <= 0
                        || request.getLength() == null || request.getLength() <= 0
                        || request.getAnimalTypes() == null || request.getAnimalTypes().size() == 0)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or many parameters are bad");
        }
        List<AnimalType> animalTypes = new ArrayList<>();
        for (Long id : request.getAnimalTypes()) {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or many parameters are bad");
            }
            if (animalTypes.stream().anyMatch(animalType -> animalType.getId().equals(id))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate types");
            }
            AnimalType newAnimType = animalTypeRepository.findById(id).orElse(null);
            if (newAnimType == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of type is unregistered");
            }
            animalTypes.add(newAnimType);
        }
        User chipper = userRepository.findById(request.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such id is not found");
        }
        Location chippingLocation = locationRepository.findById(request.getChippingLocationId()).orElse(null);
        if (chippingLocation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }

        Animal animal = new Animal();
        animal.setAnimalTypes(animalTypes);
        animal.setWeight(request.getWeight());
        animal.setHeight(request.getHeight());
        animal.setLength(request.getLength());
        animal.setChipper(chipper);
        animal.setVisitedLocations(new ArrayList<>());
        animal.setChippingLocation(chippingLocation);
        animal.setGender(GenderEnum.valueOf(request.getGender()));
        animal.setLifeStatus(LifeStatusEnum.ALIVE);
        animal.setChippingDateTime(new Date());
        animalRepository.save(animal);

        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.mapEntityAnimalToDto(animal));
    }

    @PutMapping("/animals/{animalId}")
    public ResponseEntity<ResponseAnimalDto> changeAnimal(@PathVariable("animalId") Long animalId, @RequestBody ChangeRequestAnimalDto request) {
        if (animalId == null || animalId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No animal with such id found");
        }

        if (request.getChipperId() == null || request.getChipperId() <= 0
                || request.getChippingLocationId() == null || request.getChippingLocationId() <= 0
                || request.getGender() == null || !EnumUtils.doesGenderEnumContainValue(request.getGender())
                || request.getHeight() == null || request.getHeight() <= 0
                || request.getWeight() == null || request.getWeight() <= 0
                || request.getLength() == null || request.getLength() <= 0
                || request.getLifeStatus() == null || !EnumUtils.doesLifeStatusEnumContainValue(request.getLifeStatus())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or many parameters are bad");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No animal with such id is found");
        }

        User chipper = userRepository.findById(request.getChipperId()).orElse(null);
        if (chipper == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such id is not found");
        }

        Location chippingLocation = locationRepository.findById(request.getChippingLocationId()).orElse(null);
        if (chippingLocation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with such id is not found");
        }

        if (animal.getLifeStatus() == LifeStatusEnum.DEAD && request.getLifeStatus().equals(LifeStatusEnum.ALIVE.getValue())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't resurrect animals");
        }

        List<VisitedLocation> visitedLocations = animal.getVisitedLocations().stream().sorted(Comparator.comparing(VisitedLocation::getDateTimeOfVisitLocationPoint)).toList();
        if(visitedLocations.size() >0 ) {
            if (request.getChippingLocationId().equals(visitedLocations.get(0).getLocationPoint().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chipping location can't be first visited place");
            }
        }

        animal.setLength(request.getLength());
        animal.setWeight(request.getWeight());
        animal.setHeight(request.getHeight());
        animal.setChipper(chipper);
        animal.setChippingLocation(chippingLocation);
        animal.setLifeStatus(LifeStatusEnum.valueOf(request.getLifeStatus()));
        animal.setGender(GenderEnum.valueOf(request.getGender()));
        if (request.getLifeStatus().equals(LifeStatusEnum.DEAD.getValue())) {
            animal.setDeathDateTime(new Date());
        }
        animalRepository.save(animal);
        return ResponseEntity.ok(DtoMapper.mapEntityAnimalToDto(animal));
    }

    @DeleteMapping("/animals/{animalId}")
    public ResponseEntity<EmptyResponse> deleteAnimal(
            @PathVariable("animalId") Long animalId
    ) {
        if (animalId == null || animalId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        if (animal.getVisitedLocations().size() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal has visited locations");
        }

        animalRepository.delete(animal);
        return ResponseEntity.ok(new EmptyResponse());
    }

    @PostMapping("/animals/{animalId}/types/{typeId}")
    public ResponseEntity<ResponseAnimalDto> addTpyeToAnimal(
            @PathVariable("animalId") Long animalId,
            @PathVariable("typeId") Long typeId
    ) {
        if (animalId == null || animalId <= 0
                || typeId == null || typeId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        AnimalType addedType = animalTypeRepository.findById(typeId).orElse(null);
        if (addedType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal type with such id is not found");
        }

        if(animal.getAnimalTypes() == null){
            animal.setAnimalTypes(new ArrayList<>());
        }

        if (animal.getAnimalTypes().stream().anyMatch(type -> type.getId().equals(typeId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal type with such id is not found");
        }

        animal.getAnimalTypes().add(addedType);
        animalRepository.save(animal);

        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.mapEntityAnimalToDto(animal));
    }

    @PutMapping("/animals/{animalId}/types")
    public ResponseEntity<ResponseAnimalDto> changeType(
            @PathVariable("animalId") Long animalId,
            @RequestBody ChangeRequestAnimalTypeDto request
    ) {
        if (animalId == null || animalId <= 0
                || request.getOldTypeId() == null || request.getOldTypeId() <= 0
                || request.getNewTypeId() == null || request.getNewTypeId() <= 0
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        AnimalType newType = animalTypeRepository.findById(request.getNewTypeId()).orElse(null);
        if (newType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such type id found for new id");
        }

        AnimalType oldType = animalTypeRepository.findById(request.getOldTypeId()).orElse(null);
        if (oldType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such type id found for new id");
        }

        boolean animalHasOldType = animal.getAnimalTypes().stream().anyMatch(animalType -> animalType.getId().equals(oldType.getId()));
        boolean animalHasNewType = animal.getAnimalTypes().stream().anyMatch(animalType -> animalType.getId().equals(newType.getId()));

        if(!animalHasOldType){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal doesn't have new type");
        }

        if (animalHasNewType){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Animal already has new type");
        }

        List<AnimalType> types = animal.getAnimalTypes();
        for(int i = 0; i < types.size(); i++){
            if(Objects.equals(types.get(i).getId(), oldType.getId())){
                types.set(i, newType);
                break;
            }
        }

        animalRepository.save(animal);
        return ResponseEntity.ok(DtoMapper.mapEntityAnimalToDto(animal));
    }

    @DeleteMapping("/animals/{animalId}/types/{typeId}")
    public ResponseEntity<ResponseAnimalDto> deleteType(
            @PathVariable("animalId") Long animalId,
            @PathVariable("typeId") Long typeId
    ){
        if (animalId == null || animalId <= 0
                || typeId == null || typeId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }

        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal with such id is not found");
        }

        AnimalType deletedType = animalTypeRepository.findById(typeId).orElse(null);
        if (deletedType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such type id found for new id");
        }

        if(animal.getAnimalTypes().size() == 1 && animal.getAnimalTypes().get(0).getId().equals(typeId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deleted id is only animal type and cannot be deleted");
        }

        if(animal.getAnimalTypes().stream().noneMatch(type -> type.getId().equals(deletedType.getId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such type id found for this animal");
        }

        animal.getAnimalTypes().removeIf(type -> type.getId().equals(deletedType.getId()));
        animalRepository.save(animal);
        return ResponseEntity.ok(DtoMapper.mapEntityAnimalToDto(animal));
    }

}
