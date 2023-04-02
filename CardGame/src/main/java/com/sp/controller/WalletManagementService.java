package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.User.User;
import com.sp.model.User.UserDTOMapper;
import com.sp.model.Wallet.Wallet;
import com.sp.model.Wallet.WalletDTO;
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
        
        wallet.setMoney(wallet.getMoney() + amount);

        return true;
    }
}
