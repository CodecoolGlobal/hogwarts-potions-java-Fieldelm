package com.codecool.hogwarts_potions.service;


import com.codecool.hogwarts_potions.model.*;
import com.codecool.hogwarts_potions.repository.IngredientRepository;
import com.codecool.hogwarts_potions.repository.PotionRepository;
import com.codecool.hogwarts_potions.repository.RecipeRepository;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import com.codecool.hogwarts_potions.service.constants.BrewingServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PotionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PotionRepository potionRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StudentService studentService;

    public List<Potion> getAll() {
        return potionRepository.findAll();
    }


    public Potion brewPotion(Long studentId, List<Ingredient> ingredients) {

        if (ingredients.size() > BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS) {
            return null;
        }
        List<Ingredient> sortedIngredients = ingredients.stream().sorted(Comparator.comparing(Ingredient::getName)).collect(Collectors.toList());

        Potion newPotion = createNewPotion(studentId, sortedIngredients);

        if (ingredients.size() < BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS) {
            newPotion.setBrewingStatus(BrewingStatus.BREW);
        } else if (isContainsNewIngredient(ingredients) || !isRecipeAlreadyExists(sortedIngredients)) {
            newPotion.setBrewingStatus(BrewingStatus.DISCOVERY);
            Recipe newRecipe = createRecipe(studentId, sortedIngredients);
            newPotion.setRecipe(newRecipe);
            newPotion.setName(newRecipe.getName());

        } else {
            newPotion.setBrewingStatus(BrewingStatus.REPLICA);

        }
        potionRepository.saveAndFlush(newPotion);
        return newPotion;

    }

    private Potion createNewPotion(Long studentId, List<Ingredient> ingredients) {
        Potion newPotion = new Potion();
        newPotion.setBrewerStudent(studentService.getStudentById(studentId));
        newPotion.setIngredients(ingredients);

        return newPotion;
    }

    public List<Potion> getPotionsByStudent(Long studentId) {
        List<Potion> potions = potionRepository.getPotionByBrewerStudent(studentId);
        return potions!= null ? potions: null;
    }


    //for Recipes, might worth refactor them to new RecipeService class

    private List<Recipe> getRecipesByStudent(Long id) {
        return recipeRepository.getRecipeByBrewerStudent(id);
    }

    private Recipe createRecipe(Long studentId, List<Ingredient> ingredients) {
        Recipe newRecipe = new Recipe();
        Student student = studentRepository.getById(studentId);
        List<Recipe> recipesOfStudent = getRecipesByStudent(studentId);

        int studentsRecipes = recipesOfStudent != null ? recipesOfStudent.size(): 0;
        newRecipe.setStudent(student);
        newRecipe.setName(String.format("%s's discovery #%d", student.getName(), studentsRecipes + 1));
        newRecipe.setIngredients(ingredients);
        recipeRepository.saveAndFlush(newRecipe);
        return newRecipe;
    }


    private boolean isRecipeAlreadyExists(List<Ingredient> newIngredients) {
        List<List<Ingredient>> allRecipeIngredients = recipeRepository.findAll().stream().map(Recipe::getIngredients).collect(Collectors.toList());


        for (List<Ingredient> existingIngredients : allRecipeIngredients) {
            if (existingIngredients.size() != newIngredients.size()) {
                continue;
            }
            return hasSameIngredients(existingIngredients, newIngredients);
        }
        return false;
    }

    //for Ingredients, might worth refactoring them to new ingredient service
    private boolean hasSameIngredients(List<Ingredient> thisIngredients, List<Ingredient> othersIngredients) {
        int amount = thisIngredients.size();

        if (amount != othersIngredients.size()) {
            return false;
        } else {
            for (int i = 0; i < amount; i++) {
                if (!thisIngredients.get(i).getName().equalsIgnoreCase(othersIngredients.get(i).getName())) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean isExistingIngredient(Ingredient ingredient) {
        return ingredientRepository.existsByName(ingredient.getName());
    }


    private boolean isContainsNewIngredient(List<Ingredient> ingredients) {
        boolean isContainingNew = false;
        for (Ingredient ingredient : ingredients) {
            if (!isExistingIngredient(ingredient)) {
                isContainingNew = true;
                ingredientRepository.saveAndFlush(ingredient);
            }
        }
        return isContainingNew;
    }

}
