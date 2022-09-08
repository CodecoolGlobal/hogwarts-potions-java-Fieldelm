package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.PetType;
import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.repository.RoomRepository;
//import jdk.vm.ci.code.RegisterAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentService studentService;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void addRoom(Room room) {
        roomRepository.saveAndFlush(room);
    }

    public Room getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        System.out.println(room.get());
        return room.isPresent() ? room.get() : null;
    }

    public void updateRoomById(Long id, List<Long> studentIds) {
        Room oldRoom = getRoomById(id);
        Set<Student> students = new HashSet<>();
        studentIds.stream().forEach((studId) -> {
            students.add(studentService.getStudentById(studId));
        });
        oldRoom.setResidents(students);
        roomRepository.saveAndFlush(oldRoom);

    }

    public void deleteRoomById(Long id) {
        roomRepository.deleteById(id);
    }

    private boolean roomSafeForRats(Room room) {
       // boolean safe = true;
        for (Student student : room.getResidents()) {
            if (student.getPetType() == PetType.CAT || student.getPetType() == PetType.OWL) {
               return false;

            }
        }
        return true;
    }

    public List<Room> getRoomsForRatOwners() {
        List<Room> rooms = roomRepository.findAll();


        List<Room> ratOwnersRoom = rooms.stream().filter(room -> roomSafeForRats(room)).collect(Collectors.toList());

        return ratOwnersRoom;
    }
}
