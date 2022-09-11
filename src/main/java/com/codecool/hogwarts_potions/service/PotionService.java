package com.codecool.hogwarts_potions.service;


import com.codecool.hogwarts_potions.model.*;
import com.codecool.hogwarts_potions.repository.PotionRepository;
import com.codecool.hogwarts_potions.service.constants.BrewingServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PotionService {

    @Autowired
    private PotionRepository potionRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientService ingredientService;

    public List<Potion> getAllPotions() {
        return potionRepository.findAll();
    }


    public Potion brewPotion(Long studentId, List<Ingredient> ingredients) {

        if (ingredients.size() > BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS) {
            return null;
        }

        ingredients.forEach(ingredient -> ingredient.setName(ingredient.getName().toLowerCase()));


        List<Ingredient> sortedIngredients = ingredientService.sortIngredient(ingredients);

        List<Ingredient> persistentList = ingredientService.getPersistentList(sortedIngredients);

        String ingredientString = createIngredientString(sortedIngredients);

        Potion newPotion = createNewPotion(studentId);
        if (ingredients.size() < BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS) {
            newPotion.setBrewingStatus(BrewingStatus.BREW);
        } else {
            findBrewingStatus(newPotion, studentId, persistentList, sortedIngredients, ingredientString);
        }

        newPotion.setIngredients(persistentList);
        potionRepository.saveAndFlush(newPotion);
        return newPotion;

    }

    private void findBrewingStatus(Potion newPotion, Long studentId, List<Ingredient> persistentList, List<Ingredient> sortedIngredients, String ingredientString) {
        if (ingredientService.hasNewIngredient(sortedIngredients) || !recipeService.isRecipeAlreadyExists(ingredientString)) {
            setDiscoveryBrewingStatus(newPotion, studentId, persistentList);
        } else {
            setReplicaBrewingStatus(newPotion);
        }
    }


    private void setDiscoveryBrewingStatus(Potion potion, Long studentId, List<Ingredient> ingredients) {

        potion.setBrewingStatus(BrewingStatus.DISCOVERY);
        Recipe newRecipe = recipeService.createRecipe(studentId, ingredients);
        potion.setRecipe(newRecipe);
        potion.setName(newRecipe.getName());
    }


    private void setReplicaBrewingStatus(Potion potion) {
        potion.setBrewingStatus(BrewingStatus.REPLICA);
        potion.setName(String.format("%s's replica", potion.getBrewerStudent().getName()));
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

    private String createIngredientString(List<Ingredient> ingredients) {
        return ingredients.stream().map(Ingredient::getName).collect(Collectors.joining());
    }

    public Potion addIngredientToPotion(Long id, Ingredient ingredient) {
        Potion potion;

        Optional<Potion> optionalPotion = potionRepository.findById(id);
        Ingredient persistentIngredient;

        if (optionalPotion.isPresent()) {
            potion = optionalPotion.get();
            persistentIngredient = ingredientService.getPersistentIngredientToPotion(ingredient);
        } else {
            return null;
        }

        potion.addIngredient(persistentIngredient);

        List<Ingredient> sortedIngredients = ingredientService.sortIngredient(potion.getIngredients());

        String ingredientsString = createIngredientString(sortedIngredients);

        if (potion.getIngredients().size() == BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS) {
            findBrewingStatus(potion, potion.getBrewerStudent().getId(), sortedIngredients, sortedIngredients, ingredientsString);
        }

        potion.setIngredients(ingredientService.sortIngredient(potion.getIngredients()));
        potionRepository.saveAndFlush(potion);
        return potion;
    }

    public List<Recipe> getRecipesForPotion(Long id) {
        Optional<Potion> optionalPotion = potionRepository.findById(id);
        List<Recipe> allRecipes = recipeService.getAllRecipes();

        if (optionalPotion.isPresent() && allRecipes != null) {

            return allRecipes.stream().filter(recipe -> recipe.getIngredients().containsAll(optionalPotion.get().getIngredients())).collect(Collectors.toList());

        } else {
            return null;
        }
    }
}
