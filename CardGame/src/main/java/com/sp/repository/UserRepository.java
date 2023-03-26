package com.sp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sp.model.Card;
import com.sp.model.User;

import org.springframework.stereotype.Repository;


public interface UserRepository extends CrudRepository<User, Integer>{
    public Optional<User> findByUsername(String username);
    
}
