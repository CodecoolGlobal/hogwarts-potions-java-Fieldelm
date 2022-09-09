package com.codecool.hogwarts_potions.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    //@JsonIgnore
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private HouseType houseType;
    private PetType petType;

    public PetType getPetType() {
        return petType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HouseType getHouseType() {
        return houseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return getId().equals(student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
