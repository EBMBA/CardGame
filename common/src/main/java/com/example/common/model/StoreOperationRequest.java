package com.example.common.model;

import javax.validation.constraints.NotEmpty;

import com.example.common.model.Enum.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class StoreOperationRequest {
    @NotEmpty(message = "User ID cannot be empty")
    private String user_id;
    @NotEmpty(message = "Card ID cannot be empty")
    private String card_id;
    @NotEmpty(message = "Amount cannot be empty")
    private TransactionType transaction;
}
