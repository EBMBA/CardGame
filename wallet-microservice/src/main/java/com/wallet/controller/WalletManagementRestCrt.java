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
import org.springframework.web.server.ResponseStatusException;

import com.example.common.Exception.WalletAlreadyExistException;
import com.example.common.Exception.WalletNotFoundException;
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
        try {
            log.info("Getting wallet for: {}", userid);
            WalletDTO wallet = walletManagementService.getWallet(Integer.valueOf(userid));

            if (wallet == null) {
                log.error("Wallet not found for user id: {}", userid);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.ok().body(wallet);
        } catch (WalletNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet Not Found", e);
        }
    }

    @PostMapping (value = "")
    @ResponseBody
    public ResponseEntity<HttpStatus> addWallet(@RequestBody WalletOperationRequest walletOperationRequest) {
        try {
            Boolean isCreated = walletManagementService.addWallet(walletOperationRequest.getUser_id());
            return Boolean.TRUE.equals(isCreated) ? 
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (WalletNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wallet Not Created", e);
        } catch (WalletAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wallet Already Exist", e);
        }
    }

    @PutMapping(value = "/{userId}",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> doTransaction(@PathVariable String userId, @RequestBody WalletTransactionRequest transactionRequest) {
        try{
            if (Boolean.TRUE.equals(walletManagementService.doTransactionRequest(Integer.valueOf(userId) ,transactionRequest))) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } 
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        } catch (WalletNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet Not Found", e);
        }
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseBody
    public ResponseEntity<Object> deleteWallet(@PathVariable String userId) {
        try {
            if (Boolean.TRUE.equals(walletManagementService.deleteWallet(Integer.valueOf(userId)))) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } 
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        } catch (WalletNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet Not Found", e);
        }
    }

}
