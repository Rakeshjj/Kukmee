package com.kukmee.foodservice;

import java.util.List;

import com.kukmee.foodentity.FoodItem;

public interface FoodItemService {

	public FoodItem save(FoodItem foodItem);

	public FoodItem update(FoodItem foodItem, Long foodid);

	public void deleteById(Long foodid);

	public FoodItem getById(Long foodid);

	public List<FoodItem> getAll();

	public List<FoodItem> getVegFoodItems();

	public List<FoodItem> getNonVegFoodItems();

}
