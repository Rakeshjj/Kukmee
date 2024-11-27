package com.kukmee.orders;

import com.kukmee.foodentity.FoodItem;

public class OrderItemDTO {
	private FoodItem fooditem;
	private int quantity;
	private Double price;

	public OrderItemDTO(FoodItem fooditem, int quantity, Double price) {
		super();
		this.fooditem = fooditem;
		this.quantity = quantity;
		this.price = price;
	}

	public FoodItem getFooditem() {
		return fooditem;
	}

	public void setFooditem(FoodItem fooditem) {
		this.fooditem = fooditem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	// getters and setters

}
