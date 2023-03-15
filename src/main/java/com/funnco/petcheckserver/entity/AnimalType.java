package com.funnco.petcheckserver.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "animal_type")
public class AnimalType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false, unique = true)
    private String type;

    @ManyToMany(mappedBy = "animalTypes")
    List<Animal> animalList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
