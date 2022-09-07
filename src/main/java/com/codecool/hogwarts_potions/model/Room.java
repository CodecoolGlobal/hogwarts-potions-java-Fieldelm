package com.codecool.hogwarts_potions.model;

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

   /* public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void addResident(Student student) {
        if (residents.size() < capacity) {
            this.residents.add(student);
        }
    }

    public void setResidents(Set<Student> residents) {
        if (residents.size() <= capacity) {
            this.residents = residents;
        }else{
            this.residents = null;
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
