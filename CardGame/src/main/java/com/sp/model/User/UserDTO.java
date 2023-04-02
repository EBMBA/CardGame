package com.sp.model.User;

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
    private String username;
    private String name;
    // private String password;
    // private Float money;
    private List<String> roleList;

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
