package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.PetType;
import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.repository.RoomRepository;
//import jdk.vm.ci.code.RegisterAttributes;
import com.codecool.hogwarts_potions.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

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
        return room.isPresent()? room.get() : null;
    }

    public void updateRoomById(Long id, List<Long> studentIds) {
        Room oldRoom = getRoomById(id);
        Set<Student> students = new HashSet<>();
        studentIds.stream().forEach((studId)->{
            students.add(studentService.getStudentById(studId));
        });
        oldRoom.setResidents(students);

    }

    public void deleteRoomById(Long id) {
       roomRepository.deleteById(id);
    }

    public List<Room> getRoomsForRatOwners() {
       List <Room> rooms = roomRepository.findAll();
       List<Room> ratOwnersRoom = new ArrayList<>();

       for (Room room : rooms){
           boolean safeForRats = true;
           for(Student resident : room.getResidents()){
               if(resident.getPetType() == PetType.CAT || resident.getPetType()==PetType.OWL){
                   safeForRats = false;
               }
           }
           if(safeForRats){
               ratOwnersRoom.add(room);
           }
       }
        return ratOwnersRoom;
    }
}
