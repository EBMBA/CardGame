package com.example.inventorymicroservice.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.api.CardAPI;
import com.example.common.api.UserAPI;
import com.example.common.model.CardDTO;
import com.example.common.model.InventoryCreationRequest;
import com.example.common.model.InventoryDTO;
import com.example.common.model.UserDTO;
import com.example.inventorymicroservice.controller.InventoryManagementService;
import com.example.inventorymicroservice.model.Inventory;
import com.example.inventorymicroservice.repository.InventoryRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryManagementService.class)
class InventoryManagementServiceTest {
    @MockBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryManagementService inventoryManagementService;

    private UserAPI userAPI = mock(UserAPI.class, "userAPI");
    private CardAPI cardAPI = mock(CardAPI.class, "cardAPI");

    @MockBean
    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryManagementService.setCardAPI(cardAPI);
        inventoryManagementService.setUserAPI(userAPI);

    }

    @Test
    void testGetInventoryDTO_InventoryExists() {
        Integer userId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);
        inventory.setCards(Set.of(1, 2, 3));
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(userId.toString());
        CardDTO cardDTO1 = new CardDTO();
        cardDTO1.setCard_id("1");
        CardDTO cardDTO2 = new CardDTO();
        cardDTO2.setCard_id("2");
        CardDTO cardDTO3 = new CardDTO();
        cardDTO3.setCard_id("3");

        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);
        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory);
        when(cardAPI.getCard(1)).thenReturn(cardDTO1);
        when(cardAPI.getCard(2)).thenReturn(cardDTO2);
        when(cardAPI.getCard(3)).thenReturn(cardDTO3);
        when(userAPI.getUser(userId)).thenReturn(userDTO);

        InventoryDTO result = inventoryManagementService.getInventoryDTO(userId);

        assertNotNull(result);
        assertEquals(userDTO, result.getUser());
        assertEquals(3, result.getCards().size());
    }

    @Test
    void testGetInventoryDTO_InventoryNotExists() {
        Integer userId = 1;

        when(inventoryRepository.existsByUserId(userId)).thenReturn(false);

        InventoryDTO result = inventoryManagementService.getInventoryDTO(userId);

        assertNull(result);
    }

    @Test
    void testAddInventory_InventoryNotExists() {
        Integer userId = 1;
        InventoryCreationRequest inventoryCreationRequest = new InventoryCreationRequest();
        inventoryCreationRequest.setUser_id(userId);

        when(inventoryRepository.existsByUserId(userId)).thenReturn(false);
        when(cardAPI.getRandomCardId()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        when(inventoryRepository.save(Mockito.any(Inventory.class))).thenReturn(new Inventory());

        boolean result = inventoryManagementService.addInventory(inventoryCreationRequest);

        assertTrue(result);
        verify(inventoryRepository, times(1)).save(Mockito.any(Inventory.class));
    }

    @Test
    void testAddInventory_InventoryExists() {
        Integer userId = 1;
        InventoryCreationRequest inventoryCreationRequest = new InventoryCreationRequest();
        inventoryCreationRequest.setUser_id(userId);

        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = inventoryManagementService.addInventory(inventoryCreationRequest);

        assertFalse(result);
        verify(inventoryRepository, never()).save(Mockito.any(Inventory.class));
    }

    @Test
    void testAddToUserInventory_CardNotPresent() {
        Integer userId = 1;
        Integer cardId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);

        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = inventoryManagementService.addToUserInventory(userId, cardId);

        assertTrue(result);
        assertTrue(inventory.hasCard(cardId));
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testAddToUserInventory_CardAlreadyPresent() {
        Integer userId = 1;
        Integer cardId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);
        inventory.addCard(cardId);

        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = inventoryManagementService.addToUserInventory(userId, cardId);

        assertFalse(result);
        verify(inventoryRepository, never()).save(inventory);
    }

    @Test
    void testRemoveFromUserInventory_CardPresent() {
        Integer userId = 1;
        Integer cardId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);
        inventory.addCard(cardId);

        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = inventoryManagementService.removeFromUserInventory(userId, cardId);

        assertTrue(result);
        assertFalse(inventory.hasCard(cardId));
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testRemoveFromUserInventory_CardNotPresent() {
        Integer userId = 1;
        Integer cardId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);

        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryRepository.existsByUserId(userId)).thenReturn(true);

        boolean result = inventoryManagementService.removeFromUserInventory(userId, cardId);

        assertFalse(result);
        verify(inventoryRepository, never()).save(inventory);
    }

    @Test
    void testDeleteInventory_InventoryFound() {
        Integer userId = 1;
        Inventory inventory = new Inventory();
        inventory.setUserId(userId);

        when(inventoryRepository.findByUserId(userId)).thenReturn(inventory).thenReturn(null);

        boolean result = inventoryManagementService.deleteInventory(userId);

        assertTrue(result);
        verify(inventoryRepository, times(1)).delete(inventory);
    }

    @Test
    void testDeleteInventory_InventoryNotFound() {
        Integer userId = 1;

        when(inventoryRepository.findByUserId(userId)).thenReturn(null);

        boolean result = inventoryManagementService.deleteInventory(userId);

        assertFalse(result);
        verify(inventoryRepository, never()).delete(Mockito.any(Inventory.class));
    }
}
