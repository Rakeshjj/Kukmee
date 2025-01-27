package com.kukmee.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.kukmart.KukmartOrderItem;

@Service
public class InventoryService {

	@Autowired
	private ProductInventoryRepository productRepository; // Inject the product repository

	// Get all products in inventory
	public List<ProductInventory> getAllProducts() {
		return productRepository.findAll();
	}

	// Get product by its name
	public ProductInventory getProductByName(String productName) {
		return productRepository.findByName(productName);
	}

	// Check if there is enough stock for the order
	public boolean checkStockAvailability(List<KukmartOrderItem> items) {
		for (KukmartOrderItem item : items) {
			ProductInventory product = productRepository.findByName(item.getName());

			if (product == null) {
				return false; // Product not found
			}

			// Check if there is enough stock
			if (product.getStockQuantity() < item.getQuantity()) {
				return false; // Not enough stock
			}
		}
		return true; // Sufficient stock for all products
	}

	// Update stock after order creation
	public void updateStockAfterOrder(List<KukmartOrderItem> items) {
		for (KukmartOrderItem item : items) {
			ProductInventory product = productRepository.findByName(item.getName());

			if (product != null) {
				int remainingStock = product.getStockQuantity() - item.getQuantity();
				product.setStockQuantity(remainingStock);
				productRepository.save(product); // Save the updated product
			}
		}
	}

	// Add a new product to the inventory
	public ProductInventory addNewProduct(ProductInventory product) {
		return productRepository.save(product);
	}

	// Update product details in the inventory
	public ProductInventory updateProduct(ProductInventory product) {
		return productRepository.save(product);
	}

	// Remove a product from the inventory (soft delete, for example)
	public void removeProduct(Long productId) {
		productRepository.deleteById(productId);
	}
}
