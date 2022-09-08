package com.codecool.hogwarts_potions.service;


import com.codecool.hogwarts_potions.repository.IngredientRepository;
import com.codecool.hogwarts_potions.repository.PotionRepository;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PotionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PotionRepository potionRepository;

    @Autowired
    private IngredientRepository ingredientRepository;
}
