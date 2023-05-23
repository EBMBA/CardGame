package com.example.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class UserRegisterRequest implements Serializable{
    @NotEmpty(message = "Name cannot be empty")
    private String name ;
    @NotEmpty(message = "Username cannot be empty")
    private String username ;
    @NotEmpty(message = "Password cannot be empty")
    private String password ;

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
