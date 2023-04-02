package com.sp.repository;

import org.springframework.data.repository.CrudRepository;

import com.sp.model.Inventory.Inventory;
import com.sp.model.User.User;

public interface InventoryRepository extends CrudRepository<Inventory, Integer>  {
    public Inventory findByUser(User user);
    public Inventory findByUserUsername(String username);
}
