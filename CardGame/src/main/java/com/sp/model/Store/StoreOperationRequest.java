package com.sp.model.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class StoreOperationRequest {
    private String username;
    private String card_id;
    private TransactionType transaction;
}
