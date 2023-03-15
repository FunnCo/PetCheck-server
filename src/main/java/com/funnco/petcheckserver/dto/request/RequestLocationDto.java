package com.funnco.petcheckserver.dto.request;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.Location} entity
 */
public class RequestLocationDto implements Serializable {
    private final Double latitude;
    private final Double longitude;

    public RequestLocationDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLocationDto entity = (RequestLocationDto) o;
        return Objects.equals(this.latitude, entity.latitude) &&
                Objects.equals(this.longitude, entity.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "latitude = " + latitude + ", " +
                "longitude = " + longitude + ")";
    }
}