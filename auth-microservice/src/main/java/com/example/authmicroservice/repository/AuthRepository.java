package com.example.authmicroservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.authmicroservice.model.AuthUser;

public interface AuthRepository extends CrudRepository<AuthUser, Integer> {
    public AuthUser findByUsername(String username);
    public AuthUser findByUserId(Integer id);
    
}
