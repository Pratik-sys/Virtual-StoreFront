package com.ecommerce.Inventory.repository;

import com.ecommerce.Inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long > {

    List<Inventory> findByProductIdIn(List<String> pIds);

}
