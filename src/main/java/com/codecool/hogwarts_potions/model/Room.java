package com.codecool.hogwarts_potions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    //@JsonIgnore
    @Id
    @GeneratedValue
    private Long id;
    private Integer capacity;
    private HouseType houseType;
    @OneToMany
    private Set<Student> residents;

    public void setResidents(Set<Student> residents) {
        this.residents = residents;
    }

    @JsonFormat
    public Set<Student> getResidents() {
        return residents;
    }

    @Override
    public String toString(){
        String students = "";
        for(Student student: residents){
            students+= student.getName() + "| ";
        }
        return String.format("Room id: %d, residents: %s", this.id, students);
    }

    /*
       public void setCapacity(Integer capacity) {
            this.capacity = capacity;
        }*/
/*
    public void addResident(Student student) {
        if (residents.size() < capacity) {
            this.residents.add(student);
        }
    }

    */
    /*
    public Long getId() {
        return id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Set<Student> getResidents() {
        return residents;
    }*/


}
