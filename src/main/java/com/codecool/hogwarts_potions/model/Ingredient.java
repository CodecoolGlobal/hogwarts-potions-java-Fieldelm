package com.codecool.hogwarts_potions.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
