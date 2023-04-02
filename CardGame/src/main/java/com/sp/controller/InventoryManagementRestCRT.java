package com.sp.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.Card.Card;
import com.sp.model.Inventory.Inventory;
import com.sp.model.Inventory.InventoryDTO;
import com.sp.model.User.User;

@RestController
@RequestMapping("/api/inventories")
public class InventoryManagementRestCRT {
    @Autowired
    InventoryManagementService inventoryManagementService;

    @GetMapping(value = "/{username}")
    @ResponseBody
    public ResponseEntity<Object> getInventories(@PathVariable String username){

        return ResponseEntity.ok().body(inventoryManagementService.getInventoryDTO(username));
    }

    @PostMapping(value = "/",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> addInventory(@RequestBody(required = true) User user, HttpServletResponse response){
        InventoryDTO inventory = inventoryManagementService.addInventory(user);

        return ResponseEntity.ok().body(inventory);
    }

}


