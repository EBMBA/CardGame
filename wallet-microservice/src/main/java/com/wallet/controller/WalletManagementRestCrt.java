package com.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.model.WalletDTO;
import com.example.common.model.WalletOperationRequest;
import com.example.common.model.WalletTransactionRequest;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/wallets")
@Slf4j
public class WalletManagementRestCrt {
    
    @Autowired
    WalletManagementService walletManagementService;

    @GetMapping(value = "/{userid}")
    @ResponseBody
    public ResponseEntity<Object> getWallet(@PathVariable String userid) {
        log.info("Getting wallet for: {}", userid);
        WalletDTO wallet = walletManagementService.getWallet(Integer.valueOf(userid));

        if (wallet == null) {
            log.error("Wallet not found for user id: {}", userid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok().body(wallet);
    }

    @PostMapping (value = "")
    @ResponseBody
    public ResponseEntity<HttpStatus> addWallet(@RequestBody WalletOperationRequest walletOperationRequest) {
        log.info("Adding wallet for: {}", walletOperationRequest.getUser_id());
        return walletManagementService.addWallet(walletOperationRequest.getUser_id()) ?  ResponseEntity.status(HttpStatus.OK).build() : 
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(value = "/{user_id}",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> doTransaction(@PathVariable String user_id, @RequestBody WalletTransactionRequest transactionRequest) {
        log.info("Doing transaction for: {} with {}", user_id, transactionRequest);
        if (walletManagementService.doTransactionRequest(Integer.valueOf(user_id) ,transactionRequest)) {
            log.info("Transaction done for: {} with {}", user_id, transactionRequest);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } 
        log.error("Transaction not done for: {}", user_id);
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

    @DeleteMapping(value = "/{user_id}")
    @ResponseBody
    public ResponseEntity<Object> deleteWallet(@PathVariable String user_id) {
        if (walletManagementService.deleteWallet(Integer.valueOf(user_id))) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } 
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

}
