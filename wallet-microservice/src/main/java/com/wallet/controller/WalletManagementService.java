package com.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import com.sp.model.Wallet.WalletDTO;
import com.example.common.model.WalletDTO;
import com.example.common.model.WalletTransactionRequest;
import com.wallet.model.Wallet.Wallet;
import com.wallet.repository.WalletRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import com.example.common.api.UserAPI;
import com.example.common.model.UserDTO;

@Service
@Slf4j
public class WalletManagementService {
    @Autowired
    WalletRepository wRepository;

    // @Autowired
    private UserAPI userAPI;

    private static final Float MONEY_REGISTER = (float) 1000.0;
    private String USER_SERVICE_URL ;

    public WalletManagementService(@Value("${USER_SERVICE_URL}") String userServiceUrl) {
        this.USER_SERVICE_URL = userServiceUrl;
        this.userAPI = new UserAPI(USER_SERVICE_URL);
    }

    public void setUserAPI(UserAPI userAPI) {
        this.userAPI = userAPI;
    }

    // This method will return the wallet of the user with the given id
    public WalletDTO getWallet(Integer userid) {
        Wallet wallet = new Wallet();

        wallet = wRepository.findByUserId(userid);

        if(wallet == null) {
            log.error("Wallet not found for user id: {}", userid);
            return null;
        }

        // RestTemplate restTemplate = new RestTemplate();
        // UserDTO userDTO = restTemplate.getForObject(USER_SERVICE_URL  + "/" + userid , UserDTO.class);
        UserDTO userDTO = userAPI.getUser(userid);

        log.info("UserDTO get from request to the user microservice: {}", userDTO);

        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setUser(userDTO);
        walletDTO.setMoney(wallet.getMoney());

        log.info("Wallet : {}", walletDTO);

        return walletDTO;
    }

    // This method will return the wallet of the user with the given id
    public Wallet getPrivateWallet(Integer userid) {
        Wallet wallet = new Wallet();

        wallet = wRepository.findByUserId(userid); 
        
        return wallet;
    }

    // This method will add a new wallet for the user with the given id
    public Boolean addWallet(Integer user_id) {

        if(wRepository.findByUserId(user_id) != null) {
            log.error("Wallet already exists for user id: {}", user_id);
            return false;
        }

        Wallet wallet = new Wallet();

        wallet.setUserId(user_id);
        wallet.setMoney(MONEY_REGISTER);

        wallet = wRepository.save(wallet);

        if (wallet == null) {
            log.error("Wallet not created for user id: {}", user_id);
            return false;
        }
        log.info("Wallet created for user id: {}", user_id);
        return true;
    }

    // This method will update the wallet of the user 
    public Boolean updateWallet(WalletDTO wallet) {
        log.info("Updating wallet for user id: {}", wallet.getUser().getUser_id());

        Wallet w = wRepository.findByUserId(wallet.getUser().getUser_id());

        w.setMoney(wallet.getMoney());

        if(wRepository.save(w) == null) {
            log.error("Wallet not updated for user id: {}", wallet.getUser().getUser_id());
            return false;
        }
        return true;
    }

    // This method will make a transaction request for the user with the given id (add money to his wallet or withdraw money from his wallet)
    public Boolean doTransactionRequest(Integer user_id, WalletTransactionRequest transactionRequest){
        log.info("Transaction request for user id: {}", user_id);

        // Get user's wallet
        Wallet wallet = this.getPrivateWallet(user_id);
        
        if (wallet == null) {
            log.error("No Wallet Found For ID {}", user_id);
            return false;
        }

        // Check if he has enough money to make the transaction
        if (!wallet.hasEnoughMoney(transactionRequest.getAmount())) {
            log.error("NotEnoughMoney");
            return false;
        }
        
        log.info("Amount : "+ transactionRequest.getAmount() + " User Id: " + user_id + " Wallet money: " + wallet.getMoney());

        wallet.setMoney(wallet.getMoney() + transactionRequest.getAmount());

        log.info("Amount : "+ transactionRequest.getAmount() + " User Id: " + user_id + " Wallet money: " + wallet.getMoney());

        if (wRepository.save(wallet) == null) {
            log.error("Wallet not updated for user id: {}", user_id);
            return false;
        }

        log.info("Wallet updated for user id: {}", user_id);
        return true;
    }

    // This method is used to delete a wallet of a user
    public Boolean deleteWallet(Integer user_id) {
        log.info("Deleting wallet for user id: {}", user_id);

        Wallet wallet = wRepository.findByUserId(user_id);

        if (wallet == null) {
            log.error("No Wallet Found For ID {}", user_id);
            return false;
        }

        wRepository.delete(wallet);

        if (wRepository.findByUserId(user_id) != null) {
            log.error("Wallet not deleted for user id: {}", user_id);
            return false;
        }

        log.info("Wallet deleted for user id: {}", user_id);
        return true;
    }
}

