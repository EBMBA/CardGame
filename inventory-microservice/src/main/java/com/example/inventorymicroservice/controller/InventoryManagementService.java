package com.example.inventorymicroservice.controller;
import com.example.common.api.CardAPI;
import com.example.common.api.UserAPI;
import com.example.common.model.*;

import com.example.inventorymicroservice.model.Inventory;
import com.example.inventorymicroservice.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class InventoryManagementService {
    @Autowired
    InventoryRepository iRepository;

    private static final Integer NUMBER_OF_CARDS = 5;
    private final String USER_SERVICE_URL;
    private final String CARD_SERVICE_URL;

    private UserAPI userAPI;
    private CardAPI cardAPI;
    
    public InventoryManagementService(@Value("${USER_SERVICE_URL}") String userServiceUrl, @Value("${CARD_SERVICE_URL}") String cardServiceUrl) {
        this.USER_SERVICE_URL = userServiceUrl;
        this.CARD_SERVICE_URL = cardServiceUrl;
        this.userAPI = new UserAPI(USER_SERVICE_URL);
        this.cardAPI = new CardAPI(CARD_SERVICE_URL);
    }

    public void setUserAPI(UserAPI userAPI) {
        this.userAPI = userAPI;
    }

    public void setCardAPI(CardAPI cardAPI) {
        this.cardAPI = cardAPI;
    }
    
    // This method will return the inventory of the user with the given id
    public InventoryDTO getInventoryDTO(Integer userid) {
        log.info("Geting inventory for user id: {}", userid);

        if (Boolean.FALSE.equals(iRepository.existsByUserId(userid))) {
            log.error("Inventory not found for user id: {}", userid);
            return null;
        }
        Inventory inventory;

        inventory = iRepository.findByUserId(userid);

        Set<CardDTO> cards =  new HashSet<>();
        
        for (Integer cardId : inventory.getCards()) {
            CardDTO cardDTO = cardAPI.getCard(cardId);
            cards.add(cardDTO);
        }
        UserDTO userDTO = userAPI.getUser(userid);

        return new InventoryDTO(userDTO, cards);
    }

    // This method will return the inventory of the user with the given id
    public Inventory getInventory(Integer userid) {
        Inventory inventory;
        inventory = iRepository.findByUserId(userid);
        return inventory;
    }

    // This method is used to add a new inventory for a user
    public Boolean addInventory(InventoryCreationRequest inventoryCreationRequest) {
        log.info("Add inventory for user id: {}", inventoryCreationRequest.getUser_id());
        if (Boolean.TRUE.equals(iRepository.existsByUserId(inventoryCreationRequest.getUser_id()))) {
            log.error("Inventory already exists for user id: {}", inventoryCreationRequest.getUser_id());
            return false;
        }
        else {
            Set<Integer> cards = new HashSet<>();

            // RestTemplate restTemplate = new RestTemplate();

            for (int i = 1; i <= NUMBER_OF_CARDS; i++) {
                Integer cardId = cardAPI.getRandomCardId();
                // Retry while cardId is already in the set
                while (cards.contains(cardId)) {
                    cardId = cardAPI.getRandomCardId();
                }
                log.info("Adding cardId: {}", cardId);
                cards.add(cardId);
            }

            Inventory inventory = new Inventory();

            inventory.setCards(cards);
            inventory.setUserId(inventoryCreationRequest.getUser_id());
            
             
            if (iRepository.save(inventory) == null) {
                log.error("Error while saving inventory for user id: {}", inventoryCreationRequest.getUser_id());
                return false;
            }
            
            return true;
        }
    }

    // This method is used to add a card to a user's inventory
    public Boolean addToUserInventory(Integer userid, Integer cardId) {
        log.info("Adding cardId: {} to user id: {}", cardId, userid);
        Inventory inventory =  iRepository.findByUserId(userid);

        // Check if he hasn't this card yet
        if (Boolean.TRUE.equals(inventory.hasCard(cardId))) {
            log.error("User id: {} already has cardId: {}", userid, cardId);
            return false;
        }

        inventory.addCard(cardId);

        iRepository.save(inventory);
        if (Boolean.TRUE.equals(this.getInventory(userid).hasCard(cardId))) {
            log.info("CardId: {} added to user id: {}", cardId, userid);
            return true;
        }
        log.error("Error while adding cardId: {} to user id: {}", cardId, userid);
        return false;
    }

    // This method is used to remove a card from a user's inventory
    public Boolean removeFromUserInventory(Integer userid, Integer cardid) {
        log.info("Removing cardId: {} from user id: {}", cardid, userid);
        Inventory inventory = iRepository.findByUserId(userid);

        // Check if he has this card
        if (Boolean.FALSE.equals(inventory.hasCard(cardid))) {
            log.error("User id: {} hasn't cardId: {}", userid, cardid);
            return false;
        }

        inventory.removeCard(cardid);

        iRepository.save(inventory);
        if (Boolean.FALSE.equals(this.getInventory(userid).hasCard(cardid))){
            log.info("CardId: {} removed from user id: {}", cardid, userid);
            return true;
        }
        log.error("Error while removing cardId: {} from user id: {}", cardid, userid);
        return false;
    }


    // This method is used to delete the inventory of a user
    public Boolean deleteInventory(Integer userId) {
        log.info("Deleting inventory for user id: {}", userId);

        Inventory inventory = iRepository.findByUserId(userId);

        if (inventory == null) {
            log.error("No Inventory Found For ID {}", userId);
            return false;
        }

        iRepository.delete(inventory);

        if (iRepository.findByUserId(userId) != null) {
            log.error("Inventory not deleted for user id: {}", userId);
            return false;
        }

        log.info("Inventory deleted for user id: {}", userId);
        return true;
    }

}
