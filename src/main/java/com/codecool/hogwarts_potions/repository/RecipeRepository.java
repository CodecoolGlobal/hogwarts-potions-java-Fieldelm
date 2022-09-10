package com.codecool.hogwarts_potions.repository;

import com.codecool.hogwarts_potions.model.Ingredient;
import com.codecool.hogwarts_potions.model.Potion;
import com.codecool.hogwarts_potions.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  //  @Query("SELECT name, id from Recipe where student.id = :studentId")
    List<Recipe> getRecipeByStudentId(Long id);

}
