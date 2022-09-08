package com.codecool.hogwarts_potions.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Potion {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    private Student brewerStudent;
    @ManyToMany
    private List<Ingredient> ingredients;
    @ManyToOne
    private Recipe recipe;

    private BrewingStatus brewingStatus;

}
