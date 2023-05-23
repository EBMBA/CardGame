package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter  
public class WalletDTO {
    private UserDTO user;
    private Float money;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WalletDTO [user=")
                .append(user)
                .append(", money=")
                .append(money)
                .append("]");
        
        return builder.toString() ;
    }
    
}
