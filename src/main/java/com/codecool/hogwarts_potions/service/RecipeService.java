package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Ingredient;
import com.codecool.hogwarts_potions.model.Recipe;
import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StudentService studentService;


    private List<Recipe> getRecipesByStudent(Long id) {
        return recipeRepository.getRecipeByStudentId(id);
    }

    public Recipe createRecipe(Long studentId, List<Ingredient> ingredients) {
        Recipe newRecipe = new Recipe();
        Student student = studentService.getStudentById(studentId);
        List<Recipe> recipesOfStudent = getRecipesByStudent(studentId);

        int studentsRecipes = recipesOfStudent != null ? recipesOfStudent.size() : 0;
        newRecipe.setStudent(student);
        newRecipe.setName(String.format("%s's discovery #%d", student.getName(), studentsRecipes + 1));
        newRecipe.setIngredients(ingredients);

        recipeRepository.saveAndFlush(newRecipe);
        return newRecipe;
    }

    public boolean isRecipeAlreadyExists(String newPotionIngredientString) {

        List<String> allRecipeIngredients = recipeRepository.findAll().stream().map(recipe -> recipe.getIngredients().stream().map(ingredient -> ingredient.getName()).collect(Collectors.joining())).collect(Collectors.toList());

        for (String existingIngredientString : allRecipeIngredients) {
            if (existingIngredientString.equals(newPotionIngredientString)) {

                return true;
            }
        }

        return false;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
}
