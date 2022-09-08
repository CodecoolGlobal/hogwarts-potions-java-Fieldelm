package com.codecool.hogwarts_potions.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
