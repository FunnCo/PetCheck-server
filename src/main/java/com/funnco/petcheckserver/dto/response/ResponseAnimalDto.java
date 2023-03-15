package com.funnco.petcheckserver.dto.response;

import com.funnco.petcheckserver.entity.GenderEnum;
import com.funnco.petcheckserver.entity.LifeStatusEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.Animal} entity
 */
public class ResponseAnimalDto implements Serializable {
    private final Long id;
    private final List<Long> animalTypes;
    private final Float weight;
    private final Float length;
    private final Float height;
    private final GenderEnum gender;
    private final LifeStatusEnum lifeStatus;
    private final Date chippingDateTime;
    private final Integer chipperId;
    private final Long chippingLocationId;
    private final List<Long> visitedLocations;
    private final Date deathDateTime;

    public ResponseAnimalDto(Long id, List<Long> animalTypeIds, Float weight, Float length, Float height, GenderEnum gender, LifeStatusEnum lifeStatus, Date chippingDateTime, Integer chipperId, Long chippingLocationId, List<Long> visitedLocations, Date deathDateTime) {
        this.id = id;
        this.animalTypes = animalTypeIds;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.lifeStatus = lifeStatus;
        this.chippingDateTime = chippingDateTime;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
        this.visitedLocations = visitedLocations;
        this.deathDateTime = deathDateTime;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getAnimalTypes() {
        return animalTypes;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getLength() {
        return length;
    }

    public Float getHeight() {
        return height;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public LifeStatusEnum getLifeStatus() {
        return lifeStatus;
    }

    public Date getChippingDateTime() {
        return chippingDateTime;
    }

    public Integer getChipperId() {
        return chipperId;
    }

    public Long getChippingLocationId() {
        return chippingLocationId;
    }

    public List<Long> getVisitedLocations() {
        return visitedLocations;
    }

    public Date getDeathDateTime() {
        return deathDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseAnimalDto entity = (ResponseAnimalDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.animalTypes, entity.animalTypes) &&
                Objects.equals(this.weight, entity.weight) &&
                Objects.equals(this.length, entity.length) &&
                Objects.equals(this.height, entity.height) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.lifeStatus, entity.lifeStatus) &&
                Objects.equals(this.chippingDateTime, entity.chippingDateTime) &&
                Objects.equals(this.chipperId, entity.chipperId) &&
                Objects.equals(this.chippingLocationId, entity.chippingLocationId) &&
                Objects.equals(this.visitedLocations, entity.visitedLocations) &&
                Objects.equals(this.deathDateTime, entity.deathDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalTypes, weight, length, height, gender, lifeStatus, chippingDateTime, chipperId, chippingLocationId, visitedLocations, deathDateTime);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "animalTypeIds = " + animalTypes + ", " +
                "weight = " + weight + ", " +
                "length = " + length + ", " +
                "height = " + height + ", " +
                "gender = " + gender + ", " +
                "lifeStatus = " + lifeStatus + ", " +
                "chippingDateTime = " + chippingDateTime + ", " +
                "chipperId = " + chipperId + ", " +
                "chippingLocationId = " + chippingLocationId + ", " +
                "visitedLocationIds = " + visitedLocations + ", " +
                "deathDateTime = " + deathDateTime + ")";
    }
}