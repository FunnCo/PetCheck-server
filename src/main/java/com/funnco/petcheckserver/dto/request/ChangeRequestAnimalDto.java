package com.funnco.petcheckserver.dto.request;

import com.funnco.petcheckserver.entity.GenderEnum;
import com.funnco.petcheckserver.entity.LifeStatusEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.Animal} entity
 */
public class ChangeRequestAnimalDto implements Serializable {
    private final Float weight;
    private final Float length;
    private final Float height;
    private final String gender;
    private final String lifeStatus;
    private final Integer chipperId;
    private final Long chippingLocationId;

    public ChangeRequestAnimalDto(Float weight, Float length, Float height, String gender, String lifeStatus, Integer chipperId, Long chippingLocationId) {
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.lifeStatus = lifeStatus;
        this.chipperId = chipperId;
        this.chippingLocationId = chippingLocationId;
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

    public String getLifeStatus() {
        return lifeStatus;
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
        ChangeRequestAnimalDto entity = (ChangeRequestAnimalDto) o;
        return Objects.equals(this.weight, entity.weight) &&
                Objects.equals(this.length, entity.length) &&
                Objects.equals(this.height, entity.height) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.lifeStatus, entity.lifeStatus) &&
                Objects.equals(this.chipperId, entity.chipperId) &&
                Objects.equals(this.chippingLocationId, entity.chippingLocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, length, height, gender, lifeStatus, chipperId, chippingLocationId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "weight = " + weight + ", " +
                "length = " + length + ", " +
                "height = " + height + ", " +
                "gender = " + gender + ", " +
                "lifeStatus = " + lifeStatus + ", " +
                "chipperId = " + chipperId + ", " +
                "chippingLocationId = " + chippingLocationId + ")";
    }
}