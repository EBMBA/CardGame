package com.sp.model.User;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper implements Function<User, UserDTO>{
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
            user.getUsername(), 
            user.getName(), 
            // user.getPassword(), 
            user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()
            ));
    }
}
