package com.codecool.hogwarts_potions.repository;

import com.codecool.hogwarts_potions.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
