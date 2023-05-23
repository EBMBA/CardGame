package com.example.common.model;

import java.util.Set;

import com.example.common.model.CardDTO;
import com.example.common.model.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter  
public class InventoryDTO {
    private UserDTO user;
    private Set<CardDTO> cards;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("InventoryDTO [user=")
                .append(user)
                .append(", cards=")
                .append(getCards())
                .append("]");
        
        return builder.toString() ;
    }
}
