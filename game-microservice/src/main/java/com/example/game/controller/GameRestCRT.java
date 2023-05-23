package com.example.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameRestCRT {
    @Autowired
    GameService gameService;

    @PostMapping(value = "/room",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> createRoom(@RequestBody Map<String, String> value, @RequestHeader("Authorization") String requestTokenHeader){
        String jwtToken = requestTokenHeader.substring(7);
        String room_name = value.get("room_name");
        int mise = Integer.parseInt(value.get("mise"));
        if (gameService.createRoom(room_name, mise, jwtToken)) {
            return ResponseEntity.ok("Room created");
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Room already exists");
        }
    }

    @GetMapping(value = "/rooms")
    @ResponseBody
    public ResponseEntity<Object> getRooms(){
        //return ResponseEntity.ok(gameService.getRooms());
        if (gameService.getRooms() == null) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No rooms");
        }
        else {
            return ResponseEntity.ok(gameService.getRooms());
        }
    }

    @GetMapping(value = "/join/{room_name}")
    @ResponseBody
    public ResponseEntity<Object> joinRoom(@PathVariable String room_name, @RequestHeader("Authorization") String requestTokenHeader){
        String jwtToken = requestTokenHeader.substring(7);
        if (gameService.joinRoom(room_name, jwtToken)) {
            return ResponseEntity.ok("Room joined");
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Room not found or already playing");
            //return ResponseEntity.ok(gameService.joinRoom(room_name));
        }
    }

    @GetMapping(value = "/room/{room_name}")
    @ResponseBody
    public ResponseEntity<Object> getRoom(@PathVariable String room_name){
        if(gameService.getRoom(room_name) != null) {
            return ResponseEntity.ok(gameService.getRoom(room_name));
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Room not found");
        }
    }

    @PutMapping(value = "/room/{room_name}",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> updateRoom(@PathVariable String room_name, @RequestBody Map<String, String> value, @RequestHeader("Authorization") String requestTokenHeader){

        String jwtToken = requestTokenHeader.substring(7);
        int card_id = Integer.parseInt(value.get("card_id"));
        if(gameService.updateRoom(room_name, jwtToken, card_id)) {
            return ResponseEntity.ok("Room updated");
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Error updating room");
        }
    }

}
