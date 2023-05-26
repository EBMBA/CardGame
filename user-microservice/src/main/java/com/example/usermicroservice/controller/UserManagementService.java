package com.example.usermicroservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.common.Exception.UserAlreadyExistsException;
import com.example.common.Exception.UserNotFoundException;
import com.example.common.Exception.WalletAlreadyExistException;
import com.example.common.Exception.WalletNotFoundException;
import com.example.common.api.InventoryAPI;
import com.example.common.api.WalletAPI;
import com.example.common.model.InventoryCreationRequest;
import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;
import com.example.common.model.UserRegisterResponse;
import com.example.common.model.WalletOperationRequest;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.model.UserDTOMapper;
import com.example.usermicroservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManagementService {

    @Autowired
    UserRepository uRepository;

    @Autowired
    private UserDTOMapper userDTOMapper;

    private WalletAPI walletAPI;
    private InventoryAPI inventoryAPI;

    private final String WALLET_SERVICE_URL ;
    private final String INVENTORY_SERVICE_URL ;

    private static final String UserNotFound_ERROR_MESSAGE = "User not found";

    public UserManagementService(@Value("${WALLET_SERVICE_URL}") String walletServiceUrl, @Value("${INVENTORY_SERVICE_URL}") String inventoryServiceUrl) {
        this.WALLET_SERVICE_URL = walletServiceUrl;
        this.INVENTORY_SERVICE_URL = inventoryServiceUrl;
        this.walletAPI = new WalletAPI(WALLET_SERVICE_URL);
        this.inventoryAPI = new InventoryAPI(INVENTORY_SERVICE_URL);
    }

    public void setWalletAPI(WalletAPI walletAPI) {
        this.walletAPI = walletAPI;
    }

    public void setInventoryAPI(InventoryAPI inventoryAPI) {
        this.inventoryAPI = inventoryAPI;
    }

    // Create a new user
    // Create a wallet for the user
    // Create an inventory for the user
    // Return the user id
    public UserRegisterResponse register(UserRegisterRequest user) throws UserAlreadyExistsException {
        log.info("Creating user {} ", user);

        Optional<User> userOptional = uRepository.findByUsername(user.getUsername());
        if ( userOptional.isPresent()) {
            log.error("User already exists {} ", user);
            throw new UserAlreadyExistsException("User already exists");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setName(user.getName());
        
        uRepository.save(newUser);

        // Get the user from the database to get the userId
        Optional<User> userOptional2 = uRepository.findByUsername(user.getUsername());
        if (Boolean.FALSE.equals(userOptional2.isPresent())) {
            log.error("Error saving user: {} ", user);
            throw new UserAlreadyExistsException("Error saving user");
        }

        newUser = userOptional2.get();
        
        WalletOperationRequest walletOperationRequest = new WalletOperationRequest(newUser.getUserId());
        // log.info("Creating wallet for user {} ", newUser);

        try {
            Boolean walletCreationResponseEntity = walletAPI.createWallet(walletOperationRequest);
            if (Boolean.FALSE.equals(walletCreationResponseEntity)) {
                log.error("WalletNotCreated");
                // delete user
                uRepository.delete(newUser);
                log.info("User deleted for : {} because of WalletNotCreated ", newUser);
                return null;
            }
        } catch (WalletAlreadyExistException e) {
            log.error("WalletNotCreated");
            // delete user
            uRepository.delete(newUser);
            log.info("User deleted for : {} because of WalletNotCreated ", newUser);
            return null;
        }
        
        

        // log.info("Wallet created for user {} ", newUser);

        // Create inventory with 5 cards
        // log.info("Creating inventory for user {} ", newUser);
        InventoryCreationRequest inventoryCreationRequest = new InventoryCreationRequest(newUser.getUserId());
        Boolean inventoryCreationResponseEntity = inventoryAPI.createInventory(inventoryCreationRequest);

        
        if(Boolean.FALSE.equals(inventoryCreationResponseEntity)){
            log.error("InventoryNotCreated");

            // delete wallet 
            try {
                Boolean walletCreationResponseEntity = walletAPI.deleteWallet(newUser.getUserId());
                if (Boolean.FALSE.equals(walletCreationResponseEntity)) {
                    log.error("WalletNotDeleted");
                    return null;
                }
            } catch (WalletNotFoundException e) {
                log.error("WalletNotDeleted");
                return null;
            }

            // delete user
            uRepository.delete(newUser);
            log.info("User and Wallet deleted for : {} because of InventoryNotCreated ", newUser);
            return null;
        }

        // log.info("Inventory created for user {} ", newUser);

        log.info("User created: {} ", newUser);
        return new UserRegisterResponse(newUser.getUserId());
    }  

    // Update user
    // Return true if user was updated
    public Boolean updateUser(UserRegisterRequest user, Integer userId) throws UserNotFoundException {
        log.info("Updating user: {} ", user);

        Optional<User> userOptional = uRepository.findById(userId);

        if (Boolean.FALSE.equals(userOptional.isPresent())) {
            throw new UserNotFoundException(UserNotFound_ERROR_MESSAGE);
        }

        User updatedUser = userOptional.get();
        updatedUser.setUsername(user.getUsername());
        updatedUser.setName(user.getName());

        uRepository.save(updatedUser);

        
        // TODO: Check if user was updated
        // Optional<User> userOptional2 = uRepository.findById(userId);


        log.info("User updated: {} ", updatedUser);

        return true;
    }  

    // Delete user
    // Delete wallet
    // Delete inventory
    // Return true if user, wallet and inventory was deleted 
    public boolean deleteUser(Integer userId) throws UserNotFoundException{
        log.info("Deleting user: {} ", userId);

		Optional<User> u = uRepository.findByUserId(userId);

        if( Boolean.FALSE.equals(u.isPresent())){
            throw new UserNotFoundException(UserNotFound_ERROR_MESSAGE);
        }

    
        uRepository.delete(u.get());



        if(getUser(userId) != null) return false;
        
        try {
            walletAPI.deleteWallet(userId);
        } catch (WalletNotFoundException e) {
            log.error("Error deleting wallet: {} ", userId);
            // rollback
            uRepository.save(u.get());

            this.checkRollbackUser(userId);

            return false;
        }

        // log.info("Wallet deleted: {} ", userId);

        if(Boolean.FALSE.equals(inventoryAPI.deleteInventory(userId))){
            log.error("Error deleting inventory: {} ", userId);
            // rollback
            uRepository.save(u.get());

            this.checkRollbackUser(userId);

            try {
                walletAPI.createWallet(new WalletOperationRequest(userId));
            } catch (Exception e) {
                log.error("Error creating wallet: {} ", userId);
                return false;
            } 

            return false;
        }
        // log.info("Inventory deleted: {} ", userId);
        log.info("User deleted: {} ", userId);
        return true;
	}


    // Get user by username
    // Return userdto
    public UserDTO getUser(String username) throws UserNotFoundException {
        return uRepository.findByUsername(username).map(userDTOMapper).orElseThrow(
            UserNotFoundException.of(UserNotFound_ERROR_MESSAGE)
        );
    }

    // Get user by id
    // Return userdto
    public UserDTO getUser(Integer userId) throws UserNotFoundException {
        return uRepository.findById(userId).map(userDTOMapper).orElseThrow(
            UserNotFoundException.of(UserNotFound_ERROR_MESSAGE)
        );
    }

    // Get all users
    // Return list of userdto
   	public List<UserDTO> getUsers() {
		List<User> userList = new ArrayList<>();
		uRepository.findAll().forEach(s -> {
			userList.add(s);
		});
		return userList
                    .stream()
                    .map(userDTOMapper)
            .collect(Collectors.toList());
	}

    private void checkRollbackUser(Integer userId) {
        try {
            this.getUser(userId);
            log.info("User rollbacked: {} ", userId);
        } catch (UserNotFoundException e) {
            log.error("Error rollbacking user: {} ", userId);
        }
    }
}
