package com.example.storemicroservice.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.example.common.model.StoreOperationRequest;

@RestController
@RequestMapping("/api/store")
public class StoreRestCRT {

    @Autowired
    StoreService storeService;

    @PostMapping(value = "",consumes = "application/json")
    public ResponseEntity<Object> transaction(@RequestBody(required = true) StoreOperationRequest request) {
        try {
            return storeService.doTransactionCard(request) ? 
                        ResponseEntity.ok().body(null) :
                        ResponseEntity.badRequest().body(null);
        } catch (RestClientException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

}
