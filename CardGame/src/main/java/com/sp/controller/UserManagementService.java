package com.sp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.repository.UserRepository;

@Service
public class UserManagementService {

    @Autowired
    UserRepository uRepository;

    public Boolean addUser(User newUser) {
        if (uRepository.findBySurname(newUser.getSurname()).isPresent()) {
            return false;
        }

        newUser.setMoney((float) 1000.0);

        User createdUser = uRepository.save(newUser);
        System.out.println(createdUser);
        return true;
    }  

    public User getUser(Integer id) {
        Optional<User> uOptional = uRepository.findById(id);
        if (uOptional.isPresent()) {
            return uOptional.get();
        } else {
            return null;
        }
    }

    public Integer getUserBySurname(String surname, String password) {
        Optional<User> uOptional = uRepository.findBySurname(surname);
        if (uOptional.isPresent()) {
            if (uOptional.get().getPassword().contentEquals(password)) {
                return uOptional.get().getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean removeCard(User user, Card card){
        try {
            // Test if user have this card
            if( !(user.getCards().contains(card))){
                return false;
            }
            user.removeCard(card);
            uRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addCard(User user, Card card){
        // Test if card is already in the user's set
        if(user.getCards().contains(card)){
            return false;
        }
        if (user.addCard(card)) {
            uRepository.save(user);
            return true;
        }
        return false;
    }
}
