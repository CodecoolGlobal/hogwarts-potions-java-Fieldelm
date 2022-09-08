package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.service.PotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/potion")
public class PotionController {
    PotionService potionService;

    @Autowired
    public PotionController(PotionService potionService){
        this.potionService = potionService;
    }
}
