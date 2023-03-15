package com.funnco.petcheckserver.entity;

import com.funnco.petcheckserver.entity.Animal;
import com.funnco.petcheckserver.entity.Location;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "visited_location")
public class VisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "visit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeOfVisitLocationPoint;

    @ManyToOne
    private Location locationPoint;


    public Date getDateTimeOfVisitLocationPoint() {
        return dateTimeOfVisitLocationPoint;
    }

    public void setDateTimeOfVisitLocationPoint(Date dateTimeOfVisitLocationPoint) {
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
    }

    public Location getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Location locationPoint) {
        this.locationPoint = locationPoint;
    }


}
