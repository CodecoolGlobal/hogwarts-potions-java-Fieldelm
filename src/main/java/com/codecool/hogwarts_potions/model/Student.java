package com.codecool.hogwarts_potions.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @JsonIgnore
    private Long id;
    private String name;
    private HouseType houseType;
    private PetType petType;
}
