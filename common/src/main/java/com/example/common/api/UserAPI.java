package com.example.common.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.common.Exception.UserAlreadyExistsException;
import com.example.common.Exception.UserNotFoundException;
import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;
import java.util.Collections;

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

    private String userServiceUrl;

    public UserAPI(@Value("${USER_SERVICE_URL}") String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
        this.webClient = WebClient.create(userServiceUrl);
    }

    public UserAPI() {
    }

    public void setuserServiceUrl(String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
        this.webClient = WebClient.create(userServiceUrl);
    }

    // This method will return the user with the given id with the use of webclient 
    public UserDTO getUser(Integer userid) throws UserNotFoundException{
        try {
            UserDTO userDTO = webClient.get()
                .uri(userServiceUrl  + "/" + userid)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    
            log.info("UserDTO get from request to the user microservice: {}", userDTO);
            return userDTO;
        } catch (WebClientResponseException.NotFound ex) {
            throw new UserNotFoundException("User not found with id: " + userid);
        }
    }

    // This method will update the user with the given id with the use of webclient
    public Boolean updateUser(Integer userid, UserRegisterRequest userRegisterRequest) throws UserNotFoundException{
        try{
            ResponseEntity<Void> responseEntity = webClient.put()
                .uri(userServiceUrl + "/" + userid)
                .bodyValue(userRegisterRequest)
                .retrieve()
                .toEntity(Void.class)
                .block();

            if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
                log.error("Error updating the user with id: {}", userid);
                return false;
            }
            
            log.info("User with id: {} updated", userid);
            return true;
        }catch (WebClientResponseException.NotFound ex) {
            throw new UserNotFoundException("User not found with id: " + userid);
        }
    }

    // This method will delete the user with the given id with the use of webclient
    public Boolean deleteUser(Integer userid) throws UserNotFoundException {
        try{
            ResponseEntity<Void> responseEntity = webClient.delete()
                .uri(userServiceUrl + "/" + userid)
                .retrieve()
                .toEntity(Void.class)
                .block();

            if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
                log.error("Error deleting the user with id: {}", userid);
                return false;
            }
            
            log.info("User with id: {} deleted", userid);
            return true;
        } catch (WebClientResponseException.NotFound ex) {
            throw new UserNotFoundException("User not found with id: " + userid);
        }
    }

    // This method will create the user with the given id with the use of webclient
    public Boolean createUser(UserRegisterRequest userRegisterRequest) throws UserAlreadyExistsException {
        try {
            ResponseEntity<Void> responseEntity = webClient.post()
            .uri(userServiceUrl)
            .bodyValue(userRegisterRequest)
            .retrieve()
            .toEntity(Void.class)
            .block();

            if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.CREATED) {
                log.error("Error creating the user with id: {}", userRegisterRequest);
                return false;
            }

            log.info("User with id: {} created", userRegisterRequest);
            return true;
            
        } catch (WebClientResponseException.Conflict e) {
            throw new UserAlreadyExistsException("User already exists with username: " + userRegisterRequest.getUsername());
        }
        
        // ResponseEntity<Void> responseEntity = webClient.post()
        //     .uri(userServiceUrl)
        //     .bodyValue(userRegisterRequest)
        //     .retrieve()
        //     .toEntity(Void.class)
        //     .block();

        // if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
        //     log.error("Error creating the user with id: {}", userRegisterRequest);
        //     return false;
        // }
        
        // log.info("User with id: {} created", userRegisterRequest);
        // return true;
    }

    // This method will return all the users with the use of webclient
    public List<UserDTO> getUsers()  {
        ResponseEntity<List<UserDTO>> responseEntity = webClient.get()
            .uri(userServiceUrl)
            .retrieve()
            .toEntityList(UserDTO.class)
            .block();

        if ( responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error getting users");
            return Collections.emptyList();
        }

        List<UserDTO> users = responseEntity.getBody();
        log.info("Users: {}", users);
        return users;
    }

}
