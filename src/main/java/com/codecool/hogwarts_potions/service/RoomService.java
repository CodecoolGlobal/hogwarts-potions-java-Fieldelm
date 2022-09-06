package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return roomRepository.getById(id);
    }

    public void updateRoomById(Long id, Room updatedRoom) {
        Room oldRoom = roomRepository.getById(id);
        oldRoom = updatedRoom;
        roomRepository.saveAndFlush(updatedRoom);

    }

    public void deleteRoomById(Long id) {
        //TODO
    }

    public List<Room> getRoomsForRatOwners() {
        //TODO
        return null;
    }
}
