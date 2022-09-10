package com.codecool.hogwarts_potions.service;


import com.codecool.hogwarts_potions.model.*;
import com.codecool.hogwarts_potions.repository.IngredientRepository;
import com.codecool.hogwarts_potions.repository.PotionRepository;
import com.codecool.hogwarts_potions.repository.RecipeRepository;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import com.codecool.hogwarts_potions.service.constants.BrewingServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        ingredients.forEach(ingredient -> ingredient.setName(ingredient.getName().toLowerCase()));

        System.out.println(ingredients.toString());
        List<Ingredient> sortedIngredients = ingredients.stream().sorted(Comparator.comparing(Ingredient::getName)).collect(Collectors.toList());

        List<Ingredient> persistentList = getPersistentList(sortedIngredients);

        String ingredientString = sortedIngredients.stream().map(Ingredient::getName).collect(Collectors.joining());

        Potion newPotion = createNewPotion(studentId);

        if (hasNewIngredient(sortedIngredients) || !isRecipeAlreadyExists(ingredientString)) {
            newPotion.setBrewingStatus(BrewingStatus.DISCOVERY);
            Recipe newRecipe = createRecipe(studentId, persistentList);
            newPotion.setRecipe(newRecipe);
            newPotion.setName(newRecipe.getName());
        } else {
            newPotion.setBrewingStatus(BrewingStatus.REPLICA);
            newPotion.setName(String.format("%s's replica", newPotion.getBrewerStudent().getName()));
        }

        newPotion.setIngredients(persistentList);
        potionRepository.saveAndFlush(newPotion);
        return newPotion;

    }

    private Potion createNewPotion(Long studentId) {
        Potion newPotion = new Potion();
        newPotion.setBrewerStudent(studentService.getStudentById(studentId));

        return newPotion;
    }

    public List<Potion> getPotionsByStudent(Long studentId) {
        List<Potion> potions = potionRepository.getPotionsByBrewerStudentId(studentId);
        return potions != null ? potions : null;
    }


    //for Recipes, might worth refactor them to new RecipeService class

    private List<Recipe> getRecipesByStudent(Long id) {
        return recipeRepository.getRecipeByStudentId(id);
    }

    private Recipe createRecipe(Long studentId, List<Ingredient> ingredients) {
        Recipe newRecipe = new Recipe();
        Student student = studentRepository.getById(studentId);
        List<Recipe> recipesOfStudent = getRecipesByStudent(studentId);

        int studentsRecipes = recipesOfStudent != null ? recipesOfStudent.size() : 0;
        newRecipe.setStudent(student);
        newRecipe.setName(String.format("%s's discovery #%d", student.getName(), studentsRecipes + 1));
        newRecipe.setIngredients(ingredients);

        recipeRepository.saveAndFlush(newRecipe);
        return newRecipe;
    }


    private boolean isRecipeAlreadyExists(String newPotionIngredientString) {

        List<String> allRecipeIngredients = recipeRepository.findAll().stream().map(recipe -> recipe.getIngredients().stream().map(ingredient -> ingredient.getName()).collect(Collectors.joining())).collect(Collectors.toList());

        for (String existingIngredientString : allRecipeIngredients) {
            if (existingIngredientString.equals(newPotionIngredientString)) {
                return true;
            }
        }

        return false;
    }

    //for Ingredients, might worth refactoring them to new ingredient service

    private boolean hasNewIngredient(List<Ingredient> ingredients) {
        boolean isHasNew = false;
        for (Ingredient newPotionIngredient : ingredients) {
            if (!ingredientRepository.existsByName(newPotionIngredient.getName())) {
                isHasNew = true;
                ingredientRepository.saveAndFlush(newPotionIngredient);
            }
        }
        return isHasNew;
    }


    List<Ingredient> getPersistentList(List<Ingredient> ingredients) {
        List<Ingredient> properList = new ArrayList<>();
        for (Ingredient newPotionIngredient : ingredients) {
            if (!ingredientRepository.existsByName(newPotionIngredient.getName())) {
                ingredientRepository.saveAndFlush(newPotionIngredient);
            }

            properList.add(ingredientRepository.getIngredientByName(newPotionIngredient.getName()));
        }
        return properList;
    }
}
