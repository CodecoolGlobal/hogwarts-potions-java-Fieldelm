package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.model.Room;
import com.codecool.hogwarts_potions.service.RoomService;
import com.codecool.hogwarts_potions.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    RoomService roomService;


    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomService.getAllRooms();
    }

    @PostMapping
    public void addRoom(@RequestBody Room room){
        roomService.addRoom(room);
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable("id") Long id){
        return roomService.getRoomById(id);
    }

    @PutMapping("/{id}")
    public void updateRoomById(@PathVariable("id") Long id, @RequestBody Room updatedRoom){
        roomService.updateRoomById(id, updatedRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteRoomById(@PathVariable("id") Long id){
        roomService.deleteRoomById(id);
    }

    //Get rooms where no cat or owl lives
    @GetMapping("/rat-owners")
    public List<Room> getRoomsForRatOwners(){
        return roomService.getRoomsForRatOwners();
    }
}
