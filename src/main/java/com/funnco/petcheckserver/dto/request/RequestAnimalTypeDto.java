package com.funnco.petcheckserver.dto.request;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.AnimalType} entity
 */
public class RequestAnimalTypeDto implements Serializable {
    private String type;

    public RequestAnimalTypeDto() {
    }

    public RequestAnimalTypeDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAnimalTypeDto entity = (RequestAnimalTypeDto) o;
        return Objects.equals(this.type, entity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "type = " + type + ")";
    }
}