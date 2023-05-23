package com.example.common.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter  
public class UserDTO {
    private String user_id;
    private String username;
    private String name;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDTO [username=")
                .append(username)
                .append(", name=")
                .append(name)
                .append("]");
        
        return builder.toString() ;
    }

}
