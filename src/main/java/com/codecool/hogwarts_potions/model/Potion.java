package com.codecool.hogwarts_potions.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Potion {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BrewingStatus brewingStatus;

    @OneToOne
    private Student brewerStudent;

    @ManyToMany
    private List<Ingredient> ingredients;

    @ManyToOne
    private Recipe recipe;




}
