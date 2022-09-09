package com.codecool.hogwarts_potions.repository;

import com.codecool.hogwarts_potions.model.Potion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PotionRepository extends JpaRepository<Potion, Long> {

    default List<Potion> getPotionByBrewerStudent(Long id){
        return null;
    }
}
