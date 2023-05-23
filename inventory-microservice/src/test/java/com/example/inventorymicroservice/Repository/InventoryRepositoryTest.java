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
public class InventoryRepositoryTest {
    @Autowired
    InventoryRepository iRepository;

    @AfterEach
    public void cleanUp() {
        iRepository.deleteAll();
    }

    @Test
    public void testFindByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(1, 1, cardsId);
        iRepository.save(inventory);
        Inventory inventoryE = iRepository.findByUserId(1);
        assert(inventoryE.getUserId() == inventory.getUserId());
    }

    @Test
    public void testFindByUserIdNull() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(1, 1, cardsId);
        iRepository.save(inventory);
        Inventory inventoryE = iRepository.findByUserId(2);
        assert(inventoryE == null);
    }

    @Test
    public void saveInventory() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(2, 2, cardsId);
        iRepository.save(inventory);
        assert(iRepository.findByUserId(2).getUserId() == 2);
    }

    @Test
    public void testExistsByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(3, 3, cardsId);
        iRepository.save(inventory);
        assert(iRepository.existsByUserId(3) == true);
    }

    @Test
    public void testExistsByUserIdFalse() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(3, 3, cardsId);
        iRepository.save(inventory);
        assert(iRepository.existsByUserId(4) == false);
    }

    @Test
    public void testDeleteByUserId() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(4, 4, cardsId);
        iRepository.save(inventory);
        iRepository.deleteByUserId(4);
        assert(iRepository.existsByUserId(4) == false);
    }

    @Test
    public void testDeleteByUserIdFalse() {
        Set<Integer> cardsId = new HashSet<Integer>();
        Inventory inventory = new Inventory(4, 4, cardsId);
        iRepository.save(inventory);
        iRepository.deleteByUserId(5);
        assert(iRepository.existsByUserId(4) == true);
    }

}
