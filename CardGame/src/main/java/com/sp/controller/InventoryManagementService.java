package com.sp.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card.Card;
import com.sp.model.Card.CardDTO;
import com.sp.model.Inventory.Inventory;
import com.sp.model.Inventory.InventoryDTO;
import com.sp.model.User.User;
import com.sp.model.User.UserDTOMapper;
import com.sp.repository.InventoryRepository;

@Service
public class InventoryManagementService {
    @Autowired
    InventoryRepository iRepository;
    // @Autowired
    // UserManagementService userManagementService;
    @Autowired
    CardManagementService cardManagementService;

    @Autowired
    UserDTOMapper userDTOMapper;

    public InventoryDTO getInventoryDTO(String username) {
        Inventory inventory = new Inventory();

        inventory = iRepository.findByUserUsername(username);
        
        Set<CardDTO> cards =  new HashSet<>();
        inventory.getCards().forEach(card -> cards.add(cardManagementService.getCardByIdDTO(card.getCardId())));

        return new InventoryDTO(userDTOMapper.apply(inventory.getUser()), cards);
    }

    public Inventory getInventory(String username) {

        Inventory inventory = new Inventory();

        // inventory = iRepository.findByUser(user);
        inventory = iRepository.findByUserUsername(username);

        return inventory;
    }

    public InventoryDTO addInventory(User user) {
        // User user = new User();//userManagementService.getUser(username);
        Set<Card> cards = cardManagementService.getRandomCards(5); 

        Inventory inventory = new Inventory();

        inventory.setCards(cards);
        inventory.setUser(user);
        
        iRepository.save(inventory); //.orElseThrow(() -> new User)

        Set<CardDTO> cardsDTO =  new HashSet<>();
        inventory.getCards().forEach(card -> cardsDTO.add(cardManagementService.getCardByIdDTO(card.getCardId())));

        return new InventoryDTO(userDTOMapper.apply(inventory.getUser()), cardsDTO);
    }

    // TODO Change parameters
    public Boolean addToUserInventory(User user, Card card) {
        // Get user's inventory
        Inventory inventory =  iRepository.findByUser(user);


        // Check if he hasn't this card yet
        if (inventory.hasCard(card)) {
            System.err.println("AlreadyHasCard");
            return false;
        }

        inventory.addCard(card);

        System.out.println("COUCOU3");


        iRepository.save(inventory);

        return this.getInventory(user.getUsername()).hasCard(card);
    }


    public Boolean removeFromUserInventory(User user, Card card) {
        // Get user's inventory       
        Inventory inventory =  iRepository.findByUser(user);

        // Check if he has this card 
        if (!inventory.hasCard(card)) {
            System.err.println("HasNotCard");
            return false;
        }

        inventory.removeCard(card);

        iRepository.save(inventory);

        
        return !this.getInventory(user.getUsername()).hasCard(card);
    }

}
