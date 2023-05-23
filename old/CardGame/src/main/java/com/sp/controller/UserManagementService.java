package com.sp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sp.model.Role;
import com.sp.model.User.User;
import com.sp.model.User.UserDTO;
import com.sp.model.User.UserDTOMapper;
import com.sp.model.User.UserRegisterRequest;
import com.sp.repository.UserRepository;

@Service
public class UserManagementService implements UserDetailsService{

    @Autowired
    UserRepository uRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDTOMapper userDTOMapper;


    @Autowired
    WalletManagementService walletManagementService;

    @Autowired
    InventoryManagementService  inventoryManagementService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        User user = uRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        return user;
    }

    public Boolean addUser(UserRegisterRequest user) {
        System.out.println("Creating user: " + user );
        Optional<User> userOptional = uRepository.findByUsername(user.getUsername());
        if ( userOptional.isPresent()) {
            System.out.println("Error user already exists: " + user);
            return false;
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setName(user.getName());

        List<Role> authorities = new ArrayList<>();
        user.getRoleList().forEach(role -> authorities.add(new Role(role)));
        newUser.setRoleList(authorities);

        uRepository.save(newUser);

        // Create wallet
        walletManagementService.addWallet(newUser);

        // Create inventory with 5 cards
        inventoryManagementService.addInventory(newUser);

        System.out.println("User created: " + newUser );
        return true;
    }  

    public Boolean setUser(UserRegisterRequest user, String username) {
        System.out.println("Modifying user: " + user );
        Optional<User> userOptional = uRepository.findByUsername(username);
        if ( ! userOptional.isPresent()) {
            System.out.println("Error user not found: " + user);
            return false;
        }

        User newUser = userOptional.get();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setName(user.getName());

        List<Role> authorities = new ArrayList<>();
        user.getRoleList().forEach(role -> authorities.add(new Role(role)));
        newUser.setRoleList(authorities);

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
    public User getUser(Integer user_id) {
        Optional<User> uOptional = uRepository.findByUserId(user_id);
        if (uOptional.isPresent()) {
            return uOptional.get();
        } else {
            return null;
        }
    }

    public UserDTO getUserNoPwd(String username) {
		Optional<User> uOpt =uRepository.findByUsername(username);
		if( uOpt.isPresent()){
			User u=uOpt.get();
			u.setPassword("*************");
            return userDTOMapper.apply(u);
		}
		return null;
	}

    public UserDTO getUserNoPwdFromId(String user_id) {
		Optional<User> uOpt =uRepository.findByUserId(Integer.valueOf(user_id));
		if( uOpt.isPresent()){
			User u=uOpt.get();
			u.setPassword("*************");
            return userDTOMapper.apply(u);
		}
		return null;
	}

	public List<UserDTO> getAllUserNoPwd() {
		List<User> userList = new ArrayList<>();
		uRepository.findAll().forEach(s -> {
			s.setPassword("*************");
			userList.add(s);
		});
		return userList
                    .stream()
                    .map(userDTOMapper)
            .collect(Collectors.toList());
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

    // public boolean removeCard(User user, Card card){
    //     try {
    //         // Test if user have this card
    //         if( !(user.getCards().contains(card))){
    //             return false;
    //         }
    //         user.removeCard(card);
    //         uRepository.save(user);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }

    // public boolean addCard(User user, Card card){
    //     // Test if card is already in the user's set
    //     if(user.getCards().contains(card)){
    //         return false;
    //     }
    //     if (user.addCard(card)) {
    //         uRepository.save(user);
    //         return true;
    //     }
    //     return false;
    // }
}
