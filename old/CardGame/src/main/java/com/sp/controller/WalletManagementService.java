package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.User.User;
import com.sp.model.User.UserDTOMapper;
import com.sp.model.Wallet.Wallet;
import com.sp.model.Wallet.WalletDTO;
import com.sp.model.Wallet.WalletTransactionRequest;
import com.sp.repository.WalletRepository;

@Service
public class WalletManagementService {
    @Autowired
    WalletRepository wRepository;

    // @Autowired
    // UserManagementService userManagementService;

    private static final Float MONEY_REGISTER = (float) 1000.0;

    @Autowired
    UserDTOMapper userDTOMapper;

    public WalletDTO getWalletDTO(String username) {
        Wallet wallet = new Wallet();

        wallet = wRepository.findByUserUsername(username);
        
        return new WalletDTO(userDTOMapper.apply(wallet.getUser()),wallet.getMoney());
    }


    public Wallet getWallet(String username) {
        Wallet wallet = new Wallet();

        wallet = wRepository.findByUserUsername(username);
        
        return wallet;
    }

    public Wallet getWallet(Integer user_id) {
        Wallet wallet = new Wallet();

        wallet = wRepository.findByUserUserId(user_id);
        
        return wallet;
    }


    public void addWallet(User user) {
        Wallet wallet = new Wallet();

        wallet.setUser(user);
        wallet.setMoney(MONEY_REGISTER);

        wallet = wRepository.save(wallet);
    }

    public void setWallet(Wallet wallet) {
        wRepository.save(wallet);
    }

    public Boolean doTransaction(User user, Float amount){
        // Get user's wallet
        Wallet wallet = this.getWallet(user.getUsername());

        // Check if he has enough money to make the transaction
        if (!wallet.hasEnoughMoney(amount)) {
            System.err.println("NotEnoughMoney");
            return false;
        }
        
        System.out.println("Amount : "+ amount + " User Id: " + user.getUserId() + " Wallet money: " + wallet.getMoney());

        wallet.setMoney(wallet.getMoney() + amount);

        System.out.println("Amount : "+ amount + " User Id: " + user.getUserId() + " Wallet money: " + wallet.getMoney());

        try {
            wRepository.save(wallet);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean doTransactionRequest(WalletTransactionRequest transactionRequest){
        // Get user's wallet
        Wallet wallet = this.getWallet(transactionRequest.getUser_id());

        // Check if he has enough money to make the transaction
        if (!wallet.hasEnoughMoney(transactionRequest.getAmount())) {
            System.err.println("NotEnoughMoney");
            return false;
        }
        
        System.out.println("Amount : "+ transactionRequest.getAmount() + " User Id: " + transactionRequest.getUser_id() + " Wallet money: " + wallet.getMoney());

        wallet.setMoney(wallet.getMoney() + transactionRequest.getAmount());

        System.out.println("Amount : "+ transactionRequest.getAmount() + " User Id: " + transactionRequest.getUser_id() + " Wallet money: " + wallet.getMoney());

        try {
            wRepository.save(wallet);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
