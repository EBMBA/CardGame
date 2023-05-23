package com.sp.repository;

import org.springframework.data.repository.CrudRepository;

import com.sp.model.User.User;
import com.sp.model.Wallet.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Integer>{
    public Wallet findByUser(User user);
    public Wallet findByUserUsername(String username);
    public Wallet findByUserUserId(Integer user_id);

}
