package com.kukmee.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
	ProductInventory findByName(String name); // Find product by name (if needed)
}
