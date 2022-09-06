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
public class Room {

    @JsonIgnore
    private Long id;
    private Integer capacity;
    private Set<Student> residents;
}
