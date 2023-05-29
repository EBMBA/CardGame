package com.example.usermicroservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.example.usermicroservice.model.User;


public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUserId(Integer user_id);
}
