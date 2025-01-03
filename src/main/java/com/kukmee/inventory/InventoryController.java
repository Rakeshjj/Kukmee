package com.kukmee.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/add")
	public InventoryItem addItem(@RequestBody InventoryItem item) {
		return inventoryService.addItem(item);
	}

	@PutMapping("/update")
	public InventoryItem updateItem(@RequestBody InventoryItem item) {
		return inventoryService.updateItem(item);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteItem(@PathVariable Long id) {
		inventoryService.deleteItem(id);
	}

	@GetMapping("/all")
	public List<InventoryItem> getAllItems() {
		return inventoryService.getAllItems();
	}

	@GetMapping("/low-stock")
	public List<InventoryItem> getLowStockItems() {
		return inventoryService.getLowStockItems();
	}
}
