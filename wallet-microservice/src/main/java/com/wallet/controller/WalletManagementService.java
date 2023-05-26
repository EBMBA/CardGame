package com.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.common.model.WalletDTO;
import com.example.common.model.WalletTransactionRequest;
import com.wallet.model.Wallet;
import com.wallet.model.WalletMapper;
import com.wallet.repository.WalletRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import com.example.common.Exception.UserNotFoundException;
import com.example.common.Exception.WalletAlreadyExistException;
import com.example.common.Exception.WalletNotFoundException;
import com.example.common.api.UserAPI;
import com.example.common.model.UserDTO;

@Service
@Slf4j
public class WalletManagementService {
    @Autowired
    WalletRepository wRepository;

    // @Autowired
    private UserAPI userAPI;

    @Autowired 
    private WalletMapper walletMapper;

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
    public WalletDTO getWallet(Integer userid) throws WalletNotFoundException{
        UserDTO userDTO = null;
        try {
            userDTO = userAPI.getUser(userid);
        } catch (UserNotFoundException e) {
            log.error("User not found for user id: {}", userid);
            throw new WalletNotFoundException("User not found for user id: " + userid);
        }


        log.debug("UserDTO get from request to the user microservice: {}", userDTO);

        Wallet wallet = this.getPrivateWallet(userid);
        WalletDTO walletDTO = walletMapper.toDTO(wallet);
        walletDTO.setUser(userDTO);

        log.debug("Wallet : {}", walletDTO);

        return walletDTO;
    }

    // This method will return the wallet of the user with the given id
    public Wallet getPrivateWallet(Integer userid) throws WalletNotFoundException {
        Wallet wallet = null;

        wallet = wRepository.findByUserId(userid); 
        
        if (wallet == null) {
            log.error("Wallet not found for user id: {}", userid);
            throw new WalletNotFoundException("Wallet not found for user id: " + userid);
        }

        return wallet;
    }

    // This method will add a new wallet for the user with the given id
    public Boolean addWallet(Integer userId) throws WalletAlreadyExistException, WalletNotFoundException {

        if(wRepository.findByUserId(userId) != null) {
            log.error("Wallet already exists for user id: {}", userId);
            throw new WalletAlreadyExistException("Wallet already exists for user id: " + userId);
        }

        Wallet wallet = new Wallet();

        wallet.setUserId(userId);
        wallet.setMoney(MONEY_REGISTER);
        wRepository.save(wallet);

        wallet = wRepository.findByUserId(userId); 

        if (wallet == null) {
            log.error("Wallet not created for user id: {}", userId);
            throw new WalletNotFoundException("Wallet not created for user id: " + userId);
        }

        log.info("Wallet created for user id: {}", userId);
        return true;
    }

    // This method will update the wallet of the user 
    public Boolean updateWallet(WalletDTO wallet) {
        log.info("Updating wallet for user id: {}", wallet.getUser().getUser_id());

        Wallet w = wRepository.findByUserId(Integer.valueOf(wallet.getUser().getUser_id()));

        w.setMoney(wallet.getMoney());

        wRepository.save(w);

        Wallet walletUpdated = wRepository.findByUserId(Integer.valueOf(wallet.getUser().getUser_id()));

        if( walletUpdated == null || Boolean.FALSE.equals(walletUpdated.getMoney().equals(wallet.getMoney()))) {
            log.error("Wallet not updated for user id: {}", wallet.getUser().getUser_id());
            return false;
        }
        return true;
    }

    // This method will make a transaction request for the user with the given id (add money to his wallet or withdraw money from his wallet)
    public Boolean doTransactionRequest(Integer userId, WalletTransactionRequest transactionRequest) throws WalletNotFoundException{
        log.info("Transaction request for user id: {}", userId);

        // Get user's wallet
        Wallet wallet = this.getPrivateWallet(userId);
        
        if (wallet == null) {
            log.error("No Wallet Found For ID {}", userId);
            return false;
        }

        // Check if he has enough money to make the transaction
        if (Boolean.FALSE.equals(wallet.hasEnoughMoney(transactionRequest.getAmount()))) {
            log.error("NotEnoughMoney");
            return false;
        }
        
        log.info("Amount : "+ transactionRequest.getAmount() + " User Id: " + userId + " Wallet money: " + wallet.getMoney());

        wallet.setMoney(wallet.getMoney() + transactionRequest.getAmount());

        log.info("Amount : "+ transactionRequest.getAmount() + " User Id: " + userId + " Wallet money: " + wallet.getMoney());

        wRepository.save(wallet);

        Wallet walletUpdated = wRepository.findByUserId(userId);
        if (walletUpdated == null || Boolean.FALSE.equals(walletUpdated.getMoney().equals(wallet.getMoney()))) {
            log.error("Wallet not updated for user id: {}", userId);
            return false;
        }

        log.info("Wallet updated for user id: {}", userId);
        return true;
    }

    // This method is used to delete a wallet of a user
    public Boolean deleteWallet(Integer userId) throws WalletNotFoundException{
        log.info("Deleting wallet for user id: {}", userId);


        Wallet wallet = this.getPrivateWallet(userId);

        wRepository.delete(wallet);

        if (wRepository.findByUserId(userId) != null) {
            log.error("Wallet not deleted for user id: {}", userId);
            return false;
        }

        log.info("Wallet deleted for user id: {}", userId);
        return true;
    }
}

