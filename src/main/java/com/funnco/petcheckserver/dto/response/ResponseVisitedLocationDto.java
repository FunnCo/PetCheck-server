package com.funnco.petcheckserver.dto.response;

import com.funnco.petcheckserver.entity.VisitedLocation;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the {@link VisitedLocation} entity
 */
public class ResponseVisitedLocationDto implements Serializable {
    private final Long id;
    private final Date dateTimeOfVisitLocationPoint;
    private final Long locationPointId;

    public ResponseVisitedLocationDto(Long id, Date dateTimeOfVisitLocationPoint, Long locationPointId) {
        this.id = id;
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
        this.locationPointId = locationPointId;
    }

    public Long getId() {
        return id;
    }

    public Date getDateTimeOfVisitLocationPoint() {
        return dateTimeOfVisitLocationPoint;
    }

    public Long getLocationPointId() {
        return locationPointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseVisitedLocationDto entity = (ResponseVisitedLocationDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.dateTimeOfVisitLocationPoint, entity.dateTimeOfVisitLocationPoint) &&
                Objects.equals(this.locationPointId, entity.locationPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeOfVisitLocationPoint, locationPointId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "dateTimeOfVisitLocationPoint = " + dateTimeOfVisitLocationPoint + ", " +
                "locationPointId = " + locationPointId + ")";
    }
}