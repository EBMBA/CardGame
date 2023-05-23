package com.sp.model.User;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class UserRegisterRequest {
    private String name ;
    private String username ;
    private String password ;
    private List<String> roleList ;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDTO [username=")
                .append(username)
                .append(", role=")
                .append(getRoleList())
                .append(", name=")
                .append(name)
                // .append(", money=")
                // .append(money)
                .append("]");
        
        return builder.toString() ;
    }
}
