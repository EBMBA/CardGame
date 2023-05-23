
package com.example.inventorymicroservice.controller;
import com.example.common.model.InventoryCreationRequest;
import com.example.common.model.InventoryDTO;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.Enum.InventoryTransactionType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
@Slf4j
public class InventoryManagementRestCRT {
    @Autowired
    InventoryManagementService inventoryManagementService;

    @GetMapping(value = "/{userid}")
    @ResponseBody
    public ResponseEntity<Object> getInventories(@PathVariable Integer userid){
        InventoryDTO inventory = inventoryManagementService.getInventoryDTO(userid);
        if (inventory != null) {
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(null);
    }

    @PostMapping(value = "",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> addInventory(@RequestBody InventoryCreationRequest inventoryCreationRequest, HttpServletResponse response){
        if (!inventoryManagementService.addInventory(inventoryCreationRequest)){
            log.error("Inventory not created for user id: {}", inventoryCreationRequest.getUser_id());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        InventoryDTO inventory =  inventoryManagementService.getInventoryDTO(inventoryCreationRequest.getUser_id());
        if (inventory != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).build();
    }

    @PutMapping(value = "/{user_id}",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> update(@PathVariable String user_id, @RequestBody(required = true) InventoryOperationRequest inventoryOperationRequest, HttpServletResponse response){
        log.info("Updating inventory for: {}", user_id);
        Boolean inventory = false;
        if(inventoryOperationRequest.getTransaction() == InventoryTransactionType.ADD){
            inventory = inventoryManagementService.addToUserInventory(Integer.valueOf(user_id) , inventoryOperationRequest.getCard_id());
        }else if (inventoryOperationRequest.getTransaction() == InventoryTransactionType.DELETE){
            inventory = inventoryManagementService.removeFromUserInventory(Integer.valueOf(user_id), inventoryOperationRequest.getCard_id());
        }
        return inventory ? ResponseEntity.status(HttpStatus.ACCEPTED).body(null) : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

    @DeleteMapping(value = "/{user_id}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable String user_id, HttpServletResponse response){
        Boolean isDeleted = inventoryManagementService.deleteInventory(Integer.valueOf(user_id));
        return isDeleted ? ResponseEntity.status(HttpStatus.ACCEPTED).body(null) : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

}



