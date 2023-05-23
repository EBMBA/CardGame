package com.sp.model.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields
@Data  
public class InventoryOperationRequest {
    private Integer card_id;
    private InventoryTransactionType transaction;
}
