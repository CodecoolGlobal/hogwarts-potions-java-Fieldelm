package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public void addStudent(Student student){
        studentRepository.saveAndFlush(student);
    }

}
