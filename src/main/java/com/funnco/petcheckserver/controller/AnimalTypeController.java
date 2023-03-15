package com.funnco.petcheckserver.controller;

import com.funnco.petcheckserver.dto.request.RequestAnimalTypeDto;
import com.funnco.petcheckserver.dto.response.EmptyResponse;
import com.funnco.petcheckserver.entity.AnimalType;
import com.funnco.petcheckserver.repository.AnimalRepository;
import com.funnco.petcheckserver.repository.AnimalTypeRepository;
import com.funnco.petcheckserver.repository.VisitedLocationRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;

@RestController
public class AnimalTypeController {

    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalRepository animalRepository;
    private final VisitedLocationRepository visitedLocationRepository;

    public AnimalTypeController(AnimalTypeRepository animalTypeRepository, AnimalRepository animalRepository, VisitedLocationRepository visitedLocationRepository) {
        this.animalTypeRepository = animalTypeRepository;
        this.animalRepository = animalRepository;
        this.visitedLocationRepository = visitedLocationRepository;
    }

    @GetMapping("/animals/types/{typeId}")
    public AnimalType getAnimalType(@PathVariable("typeId") Long id){
        if(id==null || id<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        AnimalType type = animalTypeRepository.findById(id).orElse(null);
        if(type == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal type with such id is not found");
        }
        return type;
    }

    @PostMapping("/animals/types")
    public ResponseEntity<AnimalType> createAnimalType(@RequestBody RequestAnimalTypeDto request){
        if(StringUtils.isBlank(request.getType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad animal type");
        }
        AnimalType newType = animalTypeRepository.findAnimalTypeByType(request.getType());
        if(newType != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Such animal type already exists");
        }
        newType = new AnimalType();
        newType.setType(request.getType());
        animalTypeRepository.save(newType);
        newType = animalTypeRepository.findAnimalTypeByType(request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(newType);
    }

    @PutMapping("/animals/types/{typeId}")
    public ResponseEntity<AnimalType> changeAnimalType(@PathVariable("typeId") Long id, @RequestBody RequestAnimalTypeDto request){
        if(id==null || id<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        if(StringUtils.isBlank(request.getType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad animal type");
        }

        AnimalType changedType = animalTypeRepository.findById(id).orElse(null);
        if(changedType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No type with such id found");
        }

        AnimalType tempType = animalTypeRepository.findAnimalTypeByType(request.getType());
        if(tempType != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Such animal type already exists");
        }

        changedType.setType(request.getType());
        animalTypeRepository.save(changedType);
        tempType = animalTypeRepository.findAnimalTypeByType(request.getType());
        return ResponseEntity.ok(tempType);
    }

    @DeleteMapping("/animals/types/{typeId}")
    public ResponseEntity<EmptyResponse> deleteAnimalType(@PathVariable("typeId") Long id){
        if(id==null || id<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id");
        }
        AnimalType type = animalTypeRepository.findById(id).orElse(null);
        if(type==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No type with such id found");
        }
        if (animalRepository.existsAnimalByAnimalTypes_Type(type.getType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some animals are connected to this type");
        }

        animalTypeRepository.delete(type);
        return ResponseEntity.ok(new EmptyResponse());
    }
}
