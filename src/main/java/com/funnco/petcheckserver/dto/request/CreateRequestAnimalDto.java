package com.funnco.petcheckserver.dto.request;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.Animal} entity
 */
public class CreateRequestAnimalDto implements Serializable {
    private final List<Long> animalTypes;
    private final Float weight;
    private final Float length;
    private final Float height;
    private final String gender;
    private final Integer chipperId;
    private final Long chippingLocationId;

    public CreateRequestAnimalDto(List<Long> animalTypes, Float weight, Float length, Float height, String gender, Integer chipperId, Long chippingLocationId) {
        this.animalTypes = animalTypes;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
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

    public String getGender() {
        return gender;
    }

    public Integer getChipperId() {
        return chipperId;
    }

    public Long getChippingLocationId() {
        return chippingLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateRequestAnimalDto entity = (CreateRequestAnimalDto) o;
        return Objects.equals(this.animalTypes, entity.animalTypes) &&
                Objects.equals(this.weight, entity.weight) &&
                Objects.equals(this.length, entity.length) &&
                Objects.equals(this.height, entity.height) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.chipperId, entity.chipperId) &&
                Objects.equals(this.chippingLocationId, entity.chippingLocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalTypes, weight, length, height, gender, chipperId, chippingLocationId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "animalTypeIds = " + animalTypes + ", " +
                "weight = " + weight + ", " +
                "length = " + length + ", " +
                "height = " + height + ", " +
                "gender = " + gender + ", " +
                "chipperId = " + chipperId + ", " +
                "chippingLocationId = " + chippingLocationId + ")";
    }
}