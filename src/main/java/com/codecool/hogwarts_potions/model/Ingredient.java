package com.codecool.hogwarts_potions.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
