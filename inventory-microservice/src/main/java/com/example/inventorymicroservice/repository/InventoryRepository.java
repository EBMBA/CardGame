package com.example.inventorymicroservice.repository;

import com.example.inventorymicroservice.model.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Integer> {
    public Inventory findByUserId(Integer userid);
    public Boolean existsByUserId(Integer userid);
    public void deleteByUserId(Integer userid);
}
