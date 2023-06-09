package com.example.inventorymicroservice.Model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.inventorymicroservice.model.Inventory;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testHasCard_CardExists_ReturnsTrue() {
        Integer cardId = 1;
        inventory.addCard(cardId);

        boolean result = inventory.hasCard(cardId);

        assertTrue(result);
    }

    @Test
    void testHasCard_CardDoesNotExist_ReturnsFalse() {
        Integer cardId = 1;

        boolean result = inventory.hasCard(cardId);

        assertFalse(result);
    }

    @Test
    void testAddCard_CardAddedSuccessfully_ReturnsTrue() {
        Integer cardId = 1;

        boolean result = inventory.addCard(cardId);

        assertTrue(result);
        assertTrue(inventory.hasCard(cardId));
    }

    @Test
    void testAddCard_CardAlreadyExists_ReturnsFalse() {
        Integer cardId = 1;
        inventory.addCard(cardId);

        boolean result = inventory.addCard(cardId);

        assertFalse(result);
    }

    @Test
    void testRemoveCard_CardExistsInInventory_ReturnsTrue() {
        Integer cardId = 1;
        inventory.addCard(cardId);

        boolean result = inventory.removeCard(cardId);

        assertTrue(result);
        assertFalse(inventory.hasCard(cardId));
    }

    @Test
    void testRemoveCard_CardDoesNotExistInInventory_ReturnsFalse() {
        Integer cardId = 1;

        boolean result = inventory.removeCard(cardId);

        assertFalse(result);
    }
}