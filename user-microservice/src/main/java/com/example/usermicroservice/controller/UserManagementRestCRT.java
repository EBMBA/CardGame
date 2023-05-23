package com.example.usermicroservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping(value = {""},consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> newUser(@RequestBody UserRegisterRequest user, HttpServletResponse response) {
        log.info("Creating user {} ", user);
        UserRegisterResponse registerResponse =  uAuthService.register(user);
        if (registerResponse != null) {
            log.info("User created {} ", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
        }
        log.error("User already exists {} ", user);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
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

    @PutMapping(value="/{user_id}")
	public ResponseEntity<Object> updateUser(@PathVariable String user_id, @RequestBody UserRegisterRequest user) {
        log.info("Updating user : {} with information {}", user_id, user);
        if (! uAuthService.updateUser(user, Integer.valueOf(user_id))) {
            log.error("Can't update user : {} with information {}", user_id, user);
		    return ResponseEntity.badRequest().body(null)  ;
        }
        log.info("User updated : {} with information {}", user_id, user);
		return ResponseEntity.ok().body(null) ; 
	}
	
	@DeleteMapping(value="/{user_id}")
	public ResponseEntity<Object> deleteUser(@PathVariable String user_id) {
        log.info("Deleting user : {} ", user_id);
        if (!uAuthService.deleteUser(Integer.valueOf(user_id))) {
            log.error("Can't delete user : {} ", user_id);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }
        log.info("User deleted : {} ", user_id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) ;
	}

    @GetMapping(value = {"/{user_id}"})
    @ResponseBody
    public ResponseEntity<Object> getUser(@PathVariable String user_id,HttpServletRequest request) {
        log.info("Getting user : {} ", user_id);
        UserDTO user = uAuthService.getUser(Integer.valueOf(user_id));

        if (user != null) {
            log.info("User found : {} ", user_id);
            return ResponseEntity.ok(user);
        }
        log.error("User not found : {} ", user_id);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }   
}
