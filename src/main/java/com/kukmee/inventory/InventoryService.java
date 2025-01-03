package com.kukmee.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	public InventoryItem addItem(InventoryItem item) {
		return inventoryRepository.save(item);
	}

	public InventoryItem updateItem(InventoryItem item) {
		InventoryItem existingItem = inventoryRepository.findById(item.getId())
				.orElseThrow(() -> new RuntimeException("Item not found"));
		existingItem.setQuantity(item.getQuantity());
		existingItem.setPricePerUnit(item.getPricePerUnit());
		existingItem.setMinimumStockLevel(item.getMinimumStockLevel());
		return inventoryRepository.save(existingItem);
	}

	public void deleteItem(Long id) {
		inventoryRepository.deleteById(id);
	}

	public List<InventoryItem> getAllItems() {
		return inventoryRepository.findAll();
	}

	public List<InventoryItem> getLowStockItems() {
		return inventoryRepository.findByQuantityLessThan(10); // Example threshold
	}

	public void deductStock(String itemName, int quantity) {
		InventoryItem item = inventoryRepository.findByItemName(itemName);
		if (item.getQuantity() < quantity) {
			throw new RuntimeException("Insufficient stock for item: " + itemName);
		}
		item.setQuantity(item.getQuantity() - quantity);
		inventoryRepository.save(item);
	}
}
