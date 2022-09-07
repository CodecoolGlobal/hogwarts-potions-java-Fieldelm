package com.codecool.hogwarts_potions.repository;

import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("UPDATE Room SET residents = :students WHERE id = :id")
    default void upDateRoom(Long id, Set<Student> students){}

}
