package com.example.common.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.model.WalletDTO;
import com.example.common.model.WalletOperationRequest;
import com.example.common.model.WalletTransactionRequest;

import lombok.extern.slf4j.Slf4j;

// This class will be used to communicate with the wallet microservice
// It will be used to get the wallet information from the wallet microservice
// It will be used to update the wallet information in the wallet microservice
// It will be used to delete the wallet information in the wallet microservice
// It will be used to create the wallet information in the wallet microservice
// This class will simplify the communication between the microservices and the wallet microservice
@Slf4j
public class WalletAPI {
    private String WALLET_SERVICE_URL = "http://localhost:8088/api/wallets" ;
    private WebClient webClient;

    public WalletAPI(@Value("${WALLET_SERVICE_URL}") String walletServiceUrl) {
        this.WALLET_SERVICE_URL = walletServiceUrl;
        this.webClient = WebClient.create(WALLET_SERVICE_URL);
    }

    public WalletAPI() {
    }

    public void setWALLET_SERVICE_URL(String walletServiceUrl) {
        this.WALLET_SERVICE_URL = walletServiceUrl;
        this.webClient = WebClient.create(WALLET_SERVICE_URL);
    }

    // This method will return the wallet with the given id with the use of webclient
    // Request return OK if the wallet is found and BAD_REQUEST if the wallet is not found
    public WalletDTO getWallet(Integer user_id) {
        return webClient.get()
        .uri(WALLET_SERVICE_URL + "/" + user_id)
        .retrieve()
        .bodyToMono(WalletDTO.class)
        .block();
    }

    // This method will update the wallet with the given user id and the WalletTransactionRequest with the use of webclient
    // Request return ACCEPTED if the wallet is updated and NOT_MODIFIED if the wallet is not updated
    public Boolean updateWallet(Integer user_id, WalletTransactionRequest walletTransactionRequest) {
        ResponseEntity<Void> responseEntity = webClient.put()
            .uri(WALLET_SERVICE_URL + "/" + user_id)
            .bodyValue(walletTransactionRequest)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (responseEntity == null) {
            return false;
        }

        return responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
    }

    // This method will delete the wallet with the given user id with the use of webclient 
    // Request return ACCEPTED if the wallet is deleted and NOT_MODIFIED if the wallet is not deleted
    public Boolean deleteWallet(Integer user_id) {
        ResponseEntity<Void> responseEntity = webClient.delete()
            .uri(WALLET_SERVICE_URL + "/" + user_id)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (responseEntity == null) {
            return false;
        }
        return responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
    }

    // This method will create the wallet with the given WalletOperationRequest with the use of webclient
    // Request return OK if the wallet is created and BAD_REQUEST if the wallet is not created
    public Boolean createWallet(WalletOperationRequest walletOperationRequest) {
        ResponseEntity<Void> responseEntity = webClient.post()
            .uri(WALLET_SERVICE_URL)
            .bodyValue(walletOperationRequest)
            .retrieve()
            .toBodilessEntity()
            .block();
    
        if (responseEntity == null) {
            return false;
        }
    
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
