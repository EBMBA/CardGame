package com.example.authmicroservice.model;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String  password;
}
