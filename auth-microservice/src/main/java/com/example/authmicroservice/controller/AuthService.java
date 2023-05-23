package com.example.authmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.authmicroservice.config.RestTemplateConfig;
import com.example.authmicroservice.config.security.MyBCryptPasswordEncoder;
import com.example.authmicroservice.model.AuthUser;
import com.example.authmicroservice.repository.AuthRepository;
import com.example.common.model.UserRegisterRequest;
import com.example.common.model.UserRegisterResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService implements UserDetailsService {
    
    @Autowired
    AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate;
    private String userServiceUrl;
    
    // we will inject the RestTemplate bean and the user service url
    public AuthService(RestTemplate restTemplate, @Value("${USER_SERVICE_URL}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    // this method will be called by spring security to get the user details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = authRepository.findByUsername(username);
        if (user == null) {
            log.error("User {} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    // this method will call the user service to create a new user
    // if the user is created successfully, then we will create a new AuthUser object and save it to the database
    // we will return true if the user is created successfully, otherwise we will return false
    public Boolean register(UserRegisterRequest userRegisterRequest){
        ResponseEntity<UserRegisterResponse> responseEntity = restTemplate.postForEntity(userServiceUrl, userRegisterRequest, UserRegisterResponse.class);
        
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            log.info("Received 202 from : {}", userServiceUrl);
            AuthUser authUser = new AuthUser();
            authUser.setUsername(userRegisterRequest.getUsername());

            UserRegisterResponse registerResponse = responseEntity.getBody();

            // TODO: delete user
            if (registerResponse == null) {
                log.error("Received null response from", userServiceUrl);
                return false;
            }

            authUser.setUserId(registerResponse.getUserId());
            
            // encode password
            authUser.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

            log.info("Registering user: {}", authUser);
            return  authRepository.save(authUser) != null ? true : false;
        }
        
        log.error("Received error code from", userServiceUrl);
        return false;
    }
}
