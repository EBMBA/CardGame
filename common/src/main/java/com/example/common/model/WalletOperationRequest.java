package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class WalletOperationRequest {
    private Integer user_id;
}
