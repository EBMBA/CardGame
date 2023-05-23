package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.Store.StoreOperationRequest;

@RestController
@RequestMapping("/api/store")
public class StoreRestCRT {

    @Autowired
    StoreService storeService;

    @PostMapping(value = "/",consumes = "application/json")
    // @ResponseBody
    public ResponseEntity<Object> buyCard(@RequestBody(required = true) StoreOperationRequest request) {
        // storeService.doTransactionCard(request);
        return storeService.doTransactionCard(request) ? 
                    ResponseEntity.ok().body(null) :
                    ResponseEntity.badRequest().body(null);
    }

}
