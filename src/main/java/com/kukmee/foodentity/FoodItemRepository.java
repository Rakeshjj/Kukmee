package com.kukmee.foodentity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

	List<FoodItem> findByFoodtype(FoodType foodtype);
}
