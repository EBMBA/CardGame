package com.example.common.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// This class will be used to communicate with the user microservice
// It will be used to get the user information from the user microservice
// It will be used to update the user information in the user microservice
// It will be used to delete the user information in the user microservice
// It will be used to create the user information in the user microservice
// This class will simplify the communication between the microservices and the user microservice
@Slf4j
public class UserAPI {
    WebClient webClient;

    private String USER_SERVICE_URL = "http://localhost:8087/api/user" ;

    public UserAPI(@Value("${USER_SERVICE_URL}") String userServiceUrl) {
        this.USER_SERVICE_URL = userServiceUrl;
        this.webClient = WebClient.create(USER_SERVICE_URL);
    }

    public UserAPI() {
    }

    public void setUSER_SERVICE_URL(String userServiceUrl) {
        this.USER_SERVICE_URL = userServiceUrl;
        this.webClient = WebClient.create(USER_SERVICE_URL);
    }

    // This method will return the user with the given id with the use of webclient 
    public UserDTO getUser(Integer userid) {
        UserDTO userDTO = webClient.get()
            .uri(USER_SERVICE_URL  + "/" + userid)
            .retrieve()
            .bodyToMono(UserDTO.class)
            .block();

        log.info("UserDTO get from request to the user microservice: {}", userDTO);
        return userDTO;
    }

    // This method will update the user with the given id with the use of webclient
    public Boolean updateUser(Integer userid, UserRegisterRequest userRegisterRequest) {
        ResponseEntity<Void> responseEntity = webClient.put()
            .uri(USER_SERVICE_URL + "/" + userid)
            .bodyValue(userRegisterRequest)
            .retrieve()
            .toEntity(Void.class)
            .block();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error updating the user with id: {}", userid);
            return false;
        }
        
        log.info("User with id: {} updated", userid);
        return true;
    }

    // This method will delete the user with the given id with the use of webclient
    public Boolean deleteUser(Integer userid) {
        ResponseEntity<Void> responseEntity = webClient.delete()
            .uri(USER_SERVICE_URL + "/" + userid)
            .retrieve()
            .toEntity(Void.class)
            .block();

        if (responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            log.error("Error deleting the user with id: {}", userid);
            return false;
        }
        
        log.info("User with id: {} deleted", userid);
        return true;
    }

    // This method will create the user with the given id with the use of webclient
    public Boolean createUser(UserRegisterRequest userRegisterRequest) {
        ResponseEntity<Void> responseEntity = webClient.post()
            .uri(USER_SERVICE_URL)
            .bodyValue(userRegisterRequest)
            .retrieve()
            .toEntity(Void.class)
            .block();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error creating the user with id: {}", userRegisterRequest);
            return false;
        }
        
        log.info("User with id: {} created", userRegisterRequest);
        return true;
    }

    // This method will return all the users with the use of webclient
    public List<UserDTO> getUsers() {
        ResponseEntity<List<UserDTO>> responseEntity = webClient.get()
            .uri(USER_SERVICE_URL)
            .retrieve()
            .toEntityList(UserDTO.class)
            .block();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error getting users");
            return null;
        }

        List<UserDTO> users = responseEntity.getBody();
        log.info("Users: {}", users);
        return users;
    }

}
