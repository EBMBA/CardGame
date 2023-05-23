package com.example.inventorymicroservice.Repository;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.inventorymicroservice.model.Inventory;
import com.example.inventorymicroservice.repository.InventoryRepository;

@DataJpaTest
class InventoryRepositoryTest {
    @Autowired
    InventoryRepository iRepository;

    @AfterEach
    public void cleanUp() {
        iRepository.deleteAll();
    }

    @Test
    void testFindByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(1, 1, cardsId);
        iRepository.save(inventory);
        Inventory inventoryE = iRepository.findByUserId(1);
        assertEquals(inventoryE.getUserId(), inventory.getUserId());
        
    }

    @Test
    void testFindByUserIdNull() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(1, 1, cardsId);
        iRepository.save(inventory);
        Inventory inventoryE = iRepository.findByUserId(2);
        assertEquals(inventoryE, null);
    }

    @Test
    void saveInventory() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(2, 2, cardsId);
        iRepository.save(inventory);
        assertEquals(iRepository.findByUserId(2).getUserId(), inventory.getUserId());
    }

    @Test
    void testExistsByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(3, 3, cardsId);
        iRepository.save(inventory);
        assertEquals(iRepository.findByUserId(3).getUserId(), inventory.getUserId());
    }

    @Test
    void testExistsByUserIdFalse() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(3, 3, cardsId);
        iRepository.save(inventory);
        assertFalse(iRepository.existsByUserId(4));
    }

    @Test
    void testDeleteByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(4, 4, cardsId);
        iRepository.save(inventory);
        iRepository.deleteByUserId(4);
        assertFalse(iRepository.existsByUserId(4));
    }

    @Test
    void testDeleteByUserIdFalse() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(4, 4, cardsId);
        iRepository.save(inventory);
        iRepository.deleteByUserId(5);
        assertTrue(iRepository.existsByUserId(4));
    }

}
