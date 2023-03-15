package com.funnco.petcheckserver.repository;

import com.funnco.petcheckserver.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAnimalsByChippingDateTimeAfter(Date date);
    List<Animal> findAnimalsByChippingDateTimeBefore(Date date);

    List<Animal> findAnimalsByChipper_Id(Integer id);

    boolean existsAnimalByChippingLocation_Id(Long id);
    boolean existsAnimalByAnimalTypes_Type(String type);
    Optional<Animal> findFirstByChipper(User user);
    List<Animal> findAnimalsByChippingLocation_Id(Long id);

    List<Animal> findAnimalsByLifeStatus(LifeStatusEnum lifeStatus);

    List<Animal> findAnimalsByGender(GenderEnum gender);
}
