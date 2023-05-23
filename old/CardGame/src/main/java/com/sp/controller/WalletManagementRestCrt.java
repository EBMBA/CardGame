package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.User.User;
import com.sp.model.Wallet.Wallet;
import com.sp.model.Wallet.WalletDTO;
import com.sp.model.Wallet.WalletTransactionRequest;

@RestController
@RequestMapping("/api/wallets")
public class WalletManagementRestCrt {
    
    @Autowired
    WalletManagementService walletManagementService;

    @GetMapping(value = "/{username}")
    @ResponseBody
    public ResponseEntity<Object> getWallet(@PathVariable String username) {

        WalletDTO wallet = walletManagementService.getWalletDTO(username);

        return ResponseEntity.ok().body(wallet);
    }

    //TODO: Change to /{wallet_id} 
    @PutMapping(value = "",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> doTransaction(@RequestBody WalletTransactionRequest transactionRequest) {

        if (walletManagementService.doTransactionRequest(transactionRequest)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } 
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

}
