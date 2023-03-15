package com.funnco.petcheckserver.dto.request;

import com.funnco.petcheckserver.entity.VisitedLocation;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link VisitedLocation} entity
 */
public class RequestVisitedLocationDto implements Serializable {
    private final Long id;

    private final Long locationPointLocationPointId;

    public RequestVisitedLocationDto(Long id, Long locationPointLocationPointId) {
        this.id = id;
        this.locationPointLocationPointId = locationPointLocationPointId;
    }

    public Long getId() {
        return id;
    }



    public Long getLocationPointLocationPointId() {
        return locationPointLocationPointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestVisitedLocationDto entity = (RequestVisitedLocationDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.locationPointLocationPointId, entity.locationPointLocationPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locationPointLocationPointId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "locationPointLocationPointId = " + locationPointLocationPointId + ")";
    }
}