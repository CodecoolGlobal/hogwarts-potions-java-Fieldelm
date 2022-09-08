package com.codecool.hogwarts_potions.repository;

import com.codecool.hogwarts_potions.model.Potion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotionRepository extends JpaRepository<Potion, Long> {
}
