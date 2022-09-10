package com.codecool.hogwarts_potions.service;


import com.codecool.hogwarts_potions.model.*;
import com.codecool.hogwarts_potions.repository.IngredientRepository;
import com.codecool.hogwarts_potions.repository.PotionRepository;
import com.codecool.hogwarts_potions.repository.RecipeRepository;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import com.codecool.hogwarts_potions.service.constants.BrewingServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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


        List<Ingredient> sortedIngredients = sortIngredient(ingredients);

        List<Ingredient> persistentList = getPersistentList(sortedIngredients);

        String ingredientString = createIngredientString(sortedIngredients);

        Potion newPotion = createNewPotion(studentId);
        if(ingredients.size()<BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS){
            newPotion.setBrewingStatus(BrewingStatus.BREW);
        }else {
            checkIfReplica(newPotion, studentId, persistentList, sortedIngredients, ingredientString);
        }

        newPotion.setIngredients(persistentList);
        potionRepository.saveAndFlush(newPotion);
        return newPotion;

    }

    private List<Ingredient> sortIngredient(List<Ingredient> unsortedIngredients){
        return unsortedIngredients.stream().sorted(Comparator.comparing(Ingredient::getName)).collect(Collectors.toList());
    }
    private void checkIfReplica(Potion newPotion, Long studentId, List<Ingredient> persistentList, List<Ingredient> sortedIngredients, String ingredientString){
        if (hasNewIngredient(sortedIngredients) || !isRecipeAlreadyExists(ingredientString)) {
            setDiscoveryBrewingStatus(newPotion, studentId, persistentList);
        } else {
            setReplicaBrewingStatus(newPotion);
        }

    }


    private void setDiscoveryBrewingStatus(Potion potion, Long studentId, List<Ingredient> ingredients){

        potion.setBrewingStatus(BrewingStatus.DISCOVERY);
        Recipe newRecipe = createRecipe(studentId, ingredients);
        potion.setRecipe(newRecipe);
        potion.setName(newRecipe.getName());
    }
    private void setReplicaBrewingStatus(Potion potion){
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
    private String createIngredientString(List<Ingredient> ingredients){
        return ingredients.stream().map(Ingredient::getName).collect(Collectors.joining());
    }

    public Potion addIngredientToPotion(Long id, Ingredient ingredient) {
        Potion potion;
        String ingredientName = ingredient.getName();
        Optional<Potion> optionalPotion = potionRepository.findById(id);
        ingredient.setName(ingredient.getName().toLowerCase());
        Ingredient persistentIngredient;


        if(optionalPotion.isPresent()){
           potion = optionalPotion.get();
            if(ingredientRepository.existsByName(ingredientName)){
                persistentIngredient = ingredientRepository.getIngredientByName(ingredientName);
                potion.addIngredient(persistentIngredient);
            }
            else{
                ingredientRepository.saveAndFlush(ingredient);
                persistentIngredient = ingredientRepository.getIngredientByName(ingredientName);
                potion.addIngredient(persistentIngredient);
            }
        }else{

            return null;
        }

        List<Ingredient> sortedIngredients = sortIngredient(potion.getIngredients());

        String ingredientsString = createIngredientString(sortedIngredients);

        if(potion.getIngredients().size() == BrewingServiceConstants.MAX_INGREDIENTS_FOR_POTIONS){
            checkIfReplica(potion, potion.getBrewerStudent().getId(),sortedIngredients, sortedIngredients, ingredientsString );
        }

        potion.setIngredients(sortIngredient(potion.getIngredients()));
        potionRepository.saveAndFlush(potion);
        return potion;
    }
}
