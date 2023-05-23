package com.example.usermicroservice.model;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.common.model.UserDTO;

@Service
public class UserDTOMapper implements Function<User, UserDTO>{
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getUserId().toString(),
            user.getUsername(), 
            user.getName()
            );
    }
}
