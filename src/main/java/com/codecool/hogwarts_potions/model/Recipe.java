package com.codecool.hogwarts_potions.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
