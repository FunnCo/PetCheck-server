package com.funnco.petcheckserver.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany
    private List<AnimalType> animalTypes;

    @Column(name = "weight")
    private Float weight;
    @Column(name = "length")
    private Float length;
    @Column(name = "height")
    private Float height;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "lifeStatus")
    @Enumerated(value = EnumType.STRING)
    private LifeStatusEnum lifeStatus;

    @Column(name = "chippingDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chippingDateTime;

    @ManyToOne
    private User chipper;
    @ManyToOne
    private Location chippingLocation;

    @OneToMany
    private List<VisitedLocation> visitedLocations;

    @Column(name = "deathDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDateTime;

    public List<AnimalType> getAnimalTypes() {
        return animalTypes;
    }

    public void setAnimalTypes(List<AnimalType> animalTypes) {
        this.animalTypes = animalTypes;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public LifeStatusEnum getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(LifeStatusEnum lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public Date getChippingDateTime() {
        return chippingDateTime;
    }

    public void setChippingDateTime(Date chippingDateTime) {
        this.chippingDateTime = chippingDateTime;
    }

    public User getChipper() {
        return chipper;
    }

    public void setChipper(User chipper) {
        this.chipper = chipper;
    }

    public Location getChippingLocation() {
        return chippingLocation;
    }

    public void setChippingLocation(Location chippingLocation) {
        this.chippingLocation = chippingLocation;
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<VisitedLocation> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }

    public Date getDeathDateTime() {
        return deathDateTime;
    }

    public void setDeathDateTime(Date deathDateTime) {
        this.deathDateTime = deathDateTime;
    }
}