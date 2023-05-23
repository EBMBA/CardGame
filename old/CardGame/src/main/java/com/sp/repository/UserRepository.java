package com.sp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sp.model.User.User;


public interface UserRepository extends CrudRepository<User, Integer>{
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUserId(Integer user_id);
}
