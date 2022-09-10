package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.model.Ingredient;
import com.codecool.hogwarts_potions.model.Potion;
import com.codecool.hogwarts_potions.service.PotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/potions")
public class PotionController {
    PotionService potionService;


    @Autowired
    public PotionController(PotionService potionService) {
        this.potionService = potionService;
    }

    @GetMapping
    public List<Potion> getAllPotions(){
        return potionService.getAll();
    }

    @PostMapping("/brew/{id}")
    public Potion brewPotion(@PathVariable ("id") Long id, @RequestBody List<Ingredient> ingredients) {
        return potionService.brewPotion(id, ingredients);

    }

    @GetMapping("/{id}")
    public List<Potion> getAllPotionsForStudent(@PathVariable ("id") Long id){
        return potionService.getPotionsByStudent(id);
    }

    @PutMapping("/{potion-id}/add")
    public Object addIngredientToPotion(@PathVariable("potion-id") Long id, @RequestBody Ingredient ingredient){
        try {
            Potion potion = potionService.addIngredientToPotion(id, ingredient );
            if(potion!= null){
                return potion;
            }else{
                return String.format("No potion found with %d", id);
            }
        }catch (IllegalArgumentException e) {
            return String.format("%s, send only one ingredient", e);
        }

    }
}
