package com.funnco.petcheckserver.dto.response;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.funnco.petcheckserver.entity.Location} entity
 */
public class ResponseLocationDto implements Serializable {
    private final Long id;
    private final Double latitude;
    private final Double longitude;

    public ResponseLocationDto(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
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
        ResponseLocationDto entity = (ResponseLocationDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.latitude, entity.latitude) &&
                Objects.equals(this.longitude, entity.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "latitude = " + latitude + ", " +
                "longitude = " + longitude + ")";
    }
}