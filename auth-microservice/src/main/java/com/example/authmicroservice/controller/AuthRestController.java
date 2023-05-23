package com.example.authmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.authmicroservice.model.LoginRequest;
import com.example.authmicroservice.model.JwtResponse;
import com.example.authmicroservice.model.AuthUser;
import com.example.authmicroservice.tools.JwtTokenUtil;
import com.example.common.model.JwtValidationRequest;
import com.example.common.model.UserIdDTO;
import com.example.common.model.UserRegisterRequest;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
@CrossOrigin
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthService authService;

    // This method is used by the auth-microservice to register a new user
    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        try {
            return authService.register(userRegisterRequest) ?
                    ResponseEntity.status(HttpStatus.CREATED).body(null) : 
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }        
    }

    // This method is used by the auth-microservice to login a user
    @PostMapping(value = "/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails =  authService.loadUserByUsername(authenticationRequest.getUsername());//restTemplate.getForObject(userDetailsApiUrl,User.class);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // This method is used by the auth-microservice to validate a token when a user logs in
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // This method is used by the auth-microservice to validate a token when a user tries to access a protected resource
    @GetMapping(value = "/api/auth/validate")
    public ResponseEntity<?> validateAuthenticationToken() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/api/auth/validate")
    public ResponseEntity<?> validateAuthenticationToken(@RequestBody JwtValidationRequest validateTokenRequest) throws Exception {
        try {
            boolean isValid = jwtTokenUtil.validateToken(validateTokenRequest.getToken());
            if (isValid) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (SignatureException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (MalformedJwtException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Return the user id from the token
    @PostMapping(value = "/api/auth/user")
    public ResponseEntity<?> getUserFromToken(@RequestBody JwtValidationRequest validateTokenRequest) throws Exception {
        try {
            boolean isValid = jwtTokenUtil.validateToken(validateTokenRequest.getToken());
            if (isValid) {
                UserIdDTO userIdDTO = new UserIdDTO();
                userIdDTO.setUserId(jwtTokenUtil.getUserIdFromToken(validateTokenRequest.getToken()));
                return ResponseEntity.status(HttpStatus.OK).body(userIdDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (SignatureException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (MalformedJwtException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
