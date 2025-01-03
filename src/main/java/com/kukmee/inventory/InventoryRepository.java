package com.kukmee.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

	List<InventoryItem> findByQuantityLessThan(int quantity);

	InventoryItem findByItemName(String itemName);

}
