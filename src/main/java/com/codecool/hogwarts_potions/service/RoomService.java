package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.model.Student;
import com.codecool.hogwarts_potions.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

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

    public void updateRoomById(Long id, Room updatedRoom) {
       roomRepository.upDateRoom(id, updatedRoom.getResidents());

    }

    public void deleteRoomById(Long id) {
        //TODO
    }

    public List<Room> getRoomsForRatOwners() {
        //TODO
        return null;
    }
}
