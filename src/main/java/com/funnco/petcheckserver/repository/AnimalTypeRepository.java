package com.funnco.petcheckserver.repository;

import com.funnco.petcheckserver.entity.AnimalType;
import org.springframework.data.repository.CrudRepository;

public interface AnimalTypeRepository extends CrudRepository<AnimalType, Long> {
    AnimalType findAnimalTypeByType(String type);

}
