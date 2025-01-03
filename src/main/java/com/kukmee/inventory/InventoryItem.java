package com.kukmee.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String itemName;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private double pricePerUnit;

	@Column(nullable = false)
	private int minimumStockLevel; // For low-stock alerts

	// Getters and Setters
}
