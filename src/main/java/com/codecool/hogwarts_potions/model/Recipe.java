package com.codecool.hogwarts_potions.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    private Student student;
    @ManyToMany
    private List<Ingredient> ingredients;


}
