package com.sp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.model.UserDTO;
import com.sp.repository.UserRepository;

@Service
public class UserManagementService implements UserDetailsService{

    @Autowired
    UserRepository uRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = uRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        return user;
    }

    public Boolean addUser(UserDTO userDto) {
        System.out.println("Creating user: " + userDto );
        Optional<User> userOptional = uRepository.findByUsername(userDto.getUsername());
        if ( userOptional.isPresent()) {
            System.out.println("Error user already exists: " + userDto);
            return false;
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setName(userDto.getName());
        newUser.setMoney((float) 1000.0); // Default value of the wallet 

        uRepository.save(newUser);
        System.out.println("User created: " + newUser );
        return true;
    }  

    public Boolean setUser(UserDTO userDto, String username) {
        System.out.println("Modifying user: " + userDto );
        Optional<User> userOptional = uRepository.findByUsername(username);
        if ( ! userOptional.isPresent()) {
            System.out.println("Error user not found: " + userDto);
            return false;
        }

        User newUser = userOptional.get();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setName(userDto.getName());

        uRepository.save(newUser);
        System.out.println("User modified: " + newUser );
        return true;
    }  

    public boolean deleteUser(String username) {
		Optional<User> u =uRepository.findByUsername(username);
		if( u.isPresent()){
			uRepository.delete(u.get());
			return true;
		}
		return false;
	}


    public User getUser(String username) {
        Optional<User> uOptional = uRepository.findByUsername(username);
        if (uOptional.isPresent()) {
            return uOptional.get();
        } else {
            return null;
        }
    }

    public User getUserNoPwd(String username) {
		Optional<User> uOpt =uRepository.findByUsername(username);
		if( uOpt.isPresent()){
			User u=uOpt.get();
			u.setPassword("*************");
			return u;
		}
		return null;
	}

	public List<User> getAllUserNoPwd() {
		List<User> userList = new ArrayList<>();
		uRepository.findAll().forEach(s -> {
			s.setPassword("*************");
			userList.add(s);
		});
		return userList;
	}

    



    // public Integer getUserBySurname(String surname, String password) {
    //     Optional<User> uOptional = uRepository.findByUsername(surname);
    //     if (uOptional.isPresent()) {
    //         if (uOptional.get().getPassword().contentEquals(password)) {
    //             return uOptional.get().getId();
    //         } else {
    //             return null;
    //         }
    //     } else {
    //         return null;
    //     }
    // }

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
