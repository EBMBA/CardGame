package com.example.common.api;

import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.model.JwtValidationRequest;
import com.example.common.model.UserIdDTO;

public class AuthAPI {
    WebClient webClient;

    public AuthAPI() {
    }

    public AuthAPI(String authServiceUrl) {
        this.webClient = WebClient.create(authServiceUrl);
    }

    public void setAuthServiceUrl(String authServiceUrl) {
        this.webClient = WebClient.create(authServiceUrl);
    }
    
    public Integer getUserIdFromToken(String token) {
        JwtValidationRequest jwtValidationRequest = new JwtValidationRequest();
        jwtValidationRequest.setToken(token);
        UserIdDTO userIdDTO = this.webClient.post()
            .uri("/user")
            .header("Authorization", "Bearer " + token)
            .bodyValue(jwtValidationRequest)
            .retrieve()
            .bodyToMono(UserIdDTO.class)
            .block();
        
        if (userIdDTO == null) {
            return null;
        }
        
        return userIdDTO.getUserId();
    }
}
