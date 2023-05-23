package com.example.usermicroservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.usermicroservice.model.User;


public interface UserRepository extends CrudRepository<User, Integer>{
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUserId(Integer user_id);
}
