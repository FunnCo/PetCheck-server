package com.funnco.petcheckserver.dto.request;

public class RequestChangedVisitedLocationDto {
    private Long visitedLocationPointId;
    private Long locationPointId;

    public Long getVisitedLocationPointId() {
        return visitedLocationPointId;
    }

    public void setVisitedLocationPointId(Long visitedLocationPointId) {
        this.visitedLocationPointId = visitedLocationPointId;
    }

    public Long getLocationPointId() {
        return locationPointId;
    }

    public void setLocationPointId(Long locationPointId) {
        this.locationPointId = locationPointId;
    }

    public RequestChangedVisitedLocationDto(Long visitedLocationPointId, Long locationPointId) {
        this.visitedLocationPointId = visitedLocationPointId;
        this.locationPointId = locationPointId;
    }
}
