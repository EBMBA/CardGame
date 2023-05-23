package com.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//import com.sp.model.User.User;


import com.example.common.model.UserDTO;
import com.example.common.model.WalletDTO;
import com.wallet.model.Wallet.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Integer>{
    // public Wallet findByUser(User user);
    // public Wallet findByUserUsername(String username);
    public Wallet findByUserId(Integer id);

    public Wallet findByUserId(String user_id);
}
