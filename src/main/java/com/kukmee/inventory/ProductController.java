package com.kukmee.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private InventoryService inventoryService;

	// Endpoint to get all products in inventory
	@GetMapping("/all")
	public List<ProductInventory> getAllProducts() {
		return inventoryService.getAllProducts();
	}

	// Endpoint to get product by name
	@GetMapping("/{productName}")
	public ResponseEntity<ProductInventory> getProductByName(@PathVariable String productName) {
		ProductInventory product = inventoryService.getProductByName(productName);
		if (product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(product);
	}

	// Endpoint to add a new product to inventory
	@PostMapping("/add")
	public ResponseEntity<ProductInventory> addNewProduct(@RequestBody ProductInventory product) {
		ProductInventory newProduct = inventoryService.addNewProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

	// Endpoint to update an existing product in inventory
	@PutMapping("/update/{id}")
	public ResponseEntity<ProductInventory> updateProduct(@PathVariable Long id,
			@RequestBody ProductInventory product) {
		product.setId(id);
		ProductInventory updatedProduct = inventoryService.updateProduct(product);
		return ResponseEntity.ok(updatedProduct);
	}

	// Endpoint to remove a product from inventory
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> removeProduct(@PathVariable Long id) {
		inventoryService.removeProduct(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
