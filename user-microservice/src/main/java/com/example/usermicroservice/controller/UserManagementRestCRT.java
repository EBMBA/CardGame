package com.example.usermicroservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.common.Exception.UserAlreadyExistsException;
import com.example.common.Exception.UserNotFoundException;
import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;
import com.example.common.model.UserRegisterResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserManagementRestCRT {
    @Autowired
    UserManagementService uAuthService;

    private static final String UserNotFound_ERROR_MESSAGE = "User not found";


    @PostMapping(value = {""},consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> newUser(@RequestBody UserRegisterRequest user, HttpServletResponse response) {
        try {
            UserRegisterResponse registerResponse =  uAuthService.register(user);
            if (registerResponse != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "User Already Exists", e);
        } 
    }

    @GetMapping(value = {""})
    @ResponseBody
    public ResponseEntity<Object> getUsers(HttpServletRequest request) {
        log.info("Getting users");
        List<UserDTO> users= uAuthService.getUsers();
        if (users == null) {
            log.error("Can't get users");
            return ResponseEntity.badRequest().body(null);
        }

        log.info("Users {} ", users);
        return ResponseEntity.ok().body(users);   
    }

    @PutMapping(value="/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody @Valid UserRegisterRequest user) {
        try {
            return Boolean.TRUE.equals(uAuthService.updateUser(user, Integer.valueOf(userId))) ?
                    ResponseEntity.ok().body(null) :
                    ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, UserNotFound_ERROR_MESSAGE, e);
        }
	}
	
	@DeleteMapping(value="/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        try {
            return Boolean.TRUE.equals(uAuthService.deleteUser(Integer.valueOf(userId))) ?
             ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
             ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null) ;
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, UserNotFound_ERROR_MESSAGE, e);
        }
	}

    @GetMapping(value = {"/{userId}"})
    @ResponseBody
    public ResponseEntity<Object> getUser(@PathVariable String userId,HttpServletRequest request) throws UserNotFoundException{
        try{
            return ResponseEntity.ok(uAuthService.getUser(Integer.valueOf(userId)));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, UserNotFound_ERROR_MESSAGE, e);
        }
    }   
}
