package com.example.common.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.common.Exception.WalletAlreadyExistException;
import com.example.common.Exception.WalletNotFoundException;
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
    public WalletDTO getWallet(Integer userId) throws WalletNotFoundException {
        try {
            WalletDTO walletDTO = webClient.get()
                .uri(WALLET_SERVICE_URL + "/" + userId)
                .retrieve()
                .bodyToMono(WalletDTO.class)
                .block();

            log.info("WalletDTO get from request to the wallet microservice: {}", walletDTO);
            return walletDTO;
        } catch (WebClientResponseException.NotFound ex) {
            throw new WalletNotFoundException("Wallet not found with id: " + userId);
        } 
    }

    // This method will update the wallet with the given user id and the WalletTransactionRequest with the use of webclient
    // Request return ACCEPTED if the wallet is updated and NOT_MODIFIED if the wallet is not updated
    public Boolean updateWallet(Integer userId, WalletTransactionRequest walletTransactionRequest) throws WalletNotFoundException {
        try{
            ResponseEntity<Void> responseEntity = webClient.put()
                .uri(WALLET_SERVICE_URL + "/" + userId)
                .bodyValue(walletTransactionRequest)
                .retrieve()
                .toBodilessEntity()
                .block();

            if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
                log.error("Error while updating the wallet with the user id: " + userId);
                return false;
            }

            return responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        } catch (WebClientResponseException.NotFound ex) {
            log.error("Error while updating the wallet with the user id: " + userId);
            throw new WalletNotFoundException("Wallet not found with id: " + userId);
        }
    }

    // This method will delete the wallet with the given user id with the use of webclient 
    // Request return ACCEPTED if the wallet is deleted and NOT_MODIFIED if the wallet is not deleted
    public Boolean deleteWallet(Integer userId) throws WalletNotFoundException {
        try{
            ResponseEntity<Void> responseEntity = webClient.delete()
                .uri(WALLET_SERVICE_URL + "/" + userId)
                .retrieve()
                .toBodilessEntity()
                .block();

            if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
                log.error("Error while deleting the wallet with the user id: " + userId);
                return false;
            }
            return responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        } catch (WebClientResponseException.NotFound ex) {
            log.error("Error while deleting the wallet with the user id: " + userId);
            throw new WalletNotFoundException("Wallet not found with id: " + userId);
        }
    }

    // This method will create the wallet with the given WalletOperationRequest with the use of webclient
    // Request return OK if the wallet is created and BAD_REQUEST if the wallet is not created
    public Boolean createWallet(WalletOperationRequest walletOperationRequest) throws WalletAlreadyExistException {
        try{
            ResponseEntity<Void> responseEntity = webClient.post()
                .uri(WALLET_SERVICE_URL)
                .bodyValue(walletOperationRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
        
            if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return false;
            }
        
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (WebClientResponseException.BadRequest ex) {
            log.error("Error while creating the wallet with the user id: " + walletOperationRequest.getUser_id());
            return false;
        } catch (WebClientResponseException.Conflict ex) {
            throw new WalletAlreadyExistException("Wallet already exist with the user id: " + walletOperationRequest.getUser_id());
        }        
    }
}
