package com.example.storemicroservice.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.common.Exception.UserNotFoundException;
import com.example.common.Exception.WalletNotFoundException;
import com.example.common.api.CardAPI;
import com.example.common.api.InventoryAPI;
import com.example.common.api.UserAPI;
import com.example.common.api.WalletAPI;
import com.example.common.model.CardDTO;
import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.StoreOperationRequest;
import com.example.common.model.UserDTO;
import com.example.common.model.WalletTransactionRequest;
import com.example.common.model.Enum.InventoryTransactionType;
import com.example.common.model.Enum.TransactionType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StoreService {

    private final String WALLET_SERVICE_URL ;
    private final String INVENTORY_SERVICE_URL ;
    private final String USER_SERVICE_URL ;
    private final String CARD_SERVICE_URL ;

    private WalletAPI walletAPI;
    private InventoryAPI inventoryAPI;
    private UserAPI userAPI;
    private CardAPI cardAPI;

    public StoreService(@Value("${WALLET_SERVICE_URL}") String walletServiceUrl, @Value("${INVENTORY_SERVICE_URL}") String inventoryServiceUrl, @Value("${USER_SERVICE_URL}") String userServiceUrl, @Value("${CARD_SERVICE_URL}") String cardServiceUrl) {
        this.WALLET_SERVICE_URL = walletServiceUrl;
        this.INVENTORY_SERVICE_URL = inventoryServiceUrl;
        this.USER_SERVICE_URL = userServiceUrl;
        this.CARD_SERVICE_URL = cardServiceUrl;
        this.walletAPI = new WalletAPI(WALLET_SERVICE_URL);
        this.inventoryAPI = new InventoryAPI(INVENTORY_SERVICE_URL);
        this.userAPI = new UserAPI(USER_SERVICE_URL);
        this.cardAPI = new CardAPI(CARD_SERVICE_URL);
    }

    public void setWalletAPI(WalletAPI walletAPI) {
        this.walletAPI = walletAPI;
    }

    public void setInventoryAPI(InventoryAPI inventoryAPI) {
        this.inventoryAPI = inventoryAPI;
    }

    public void setUserAPI(UserAPI userAPI) {
        this.userAPI = userAPI;
    }

    public void setCardAPI(CardAPI cardAPI) {
        this.cardAPI = cardAPI;
    }

    // Allow to buy or sell a card
    public Boolean doTransactionCard(StoreOperationRequest storeOperationRequest) throws RestClientException, URISyntaxException{
        log.info("Making a transaction on user {} to {} card : {} ", storeOperationRequest.getUser_id(),  storeOperationRequest.getTransaction(), storeOperationRequest.getCard_id());
        
        CardDTO card = cardAPI.getCard(Integer.valueOf(storeOperationRequest.getCard_id()));
        UserDTO user;
        try {
            user = userAPI.getUser(Integer.valueOf(storeOperationRequest.getUser_id()));
        } catch (UserNotFoundException e) {
            log.error("User {} not found in the database", storeOperationRequest.getUser_id());
            return false;
        }

        // Check if card exist
        if (card == null) {
            log.error("Card {} not found in the database", storeOperationRequest.getCard_id());
            return false;            
        }

        // Do the money transaction
        Float walletTransactionAmount= storeOperationRequest.getTransaction() == TransactionType.BUY ? -card.getPrice() : card.getPrice(); 
        WalletTransactionRequest walletTransactionRequest =  new WalletTransactionRequest(walletTransactionAmount);
        Boolean walletTransactionStatus;
        try {
            walletTransactionStatus = walletAPI.updateWallet(Integer.valueOf(user.getUser_id()), walletTransactionRequest);
        } catch (WalletNotFoundException e) {
            log.error("Wallet not found for the user {} ", user.getUser_id());
            return false;
        }


        if (Boolean.FALSE.equals(walletTransactionStatus)) {
            log.error("Error in wallet transaction for the user {} ", user.getUser_id());
            return false;
        }

        InventoryOperationRequest inventoryOperationRequest =  storeOperationRequest.getTransaction() == TransactionType.BUY ?
                new InventoryOperationRequest(Integer.valueOf(card.getCard_id()), InventoryTransactionType.ADD) :
                new InventoryOperationRequest(Integer.valueOf(card.getCard_id()), InventoryTransactionType.DELETE) ; 
        Boolean inventoryTransactionStatus = inventoryAPI.updateInventory(Integer.valueOf(user.getUser_id()), inventoryOperationRequest);
        
        if(Boolean.FALSE.equals(inventoryTransactionStatus)){
            log.error("Error in inventory transaction for the user {} ", user.getUser_id());
            // Cancel the wallet transaction
            WalletTransactionRequest cancelWalletTransactionRequest = new WalletTransactionRequest(-1*walletTransactionAmount);
            Boolean cancelWalletTransactionStatus = false;
            try {
                cancelWalletTransactionStatus = walletAPI.updateWallet(Integer.valueOf(user.getUser_id()), cancelWalletTransactionRequest);
            } catch (WalletNotFoundException e) {
                log.error("Wallet not found for the user {} ", user.getUser_id());
            }
            if (Boolean.FALSE.equals(cancelWalletTransactionStatus)) {
                log.error("Error in wallet transaction cancel for the user {} ", user.getUser_id());
            }
            log.info("Transaction canceled for user {} to {} the card {}", user.getUser_id(), storeOperationRequest.getTransaction(), card.getCard_id());
            return false;
        }
        log.info("Transaction done for user {} to {} the card {}", user.getUser_id(), storeOperationRequest.getTransaction(), card.getCard_id());
        return true;
    }



}
