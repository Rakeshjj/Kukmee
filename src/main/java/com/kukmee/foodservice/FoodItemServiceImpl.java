package com.kukmee.foodservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.DuplicateValueExistsException;
import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.foodentity.FoodItem;
import com.kukmee.foodentity.FoodItemRepository;
import com.kukmee.foodentity.FoodType;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FoodItemServiceImpl implements FoodItemService {

	@Autowired
	private FoodItemRepository foodItemRepository;

	@Override
	public FoodItem save(FoodItem foodItem) {
		try {
			return foodItemRepository.save(foodItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DuplicateValueExistsException("This food alreday exists:" + foodItem.getFoodname());
		}
	}

	@Override
	public FoodItem update(FoodItem foodItem, Long foodid) {
		if (!foodItemRepository.existsById(foodid)) {
			throw new ResourceNotFoundException("food item not found:" + foodid);
		}
		foodItem.setFoodid(foodid);
		return foodItemRepository.save(foodItem);
	}

	@Override
	public void deleteById(Long foodid) {
		if (!foodItemRepository.existsById(foodid)) {
			throw new ResourceNotFoundException("food item not found:" + foodid);
		}
		foodItemRepository.deleteById(foodid);
	}

	@Override
	public FoodItem getById(Long foodid) {
		return foodItemRepository.findById(foodid)
				.orElseThrow(() -> new ResourceNotFoundException("food item not found:" + foodid));
	}

	@Override
	public List<FoodItem> getAll() {
		return foodItemRepository.findAll();
	}

	@Override
	public List<FoodItem> getVegFoodItems() {
		return foodItemRepository.findByFoodtype(FoodType.VEG);
	}

	@Override
	public List<FoodItem> getNonVegFoodItems() {
		return foodItemRepository.findByFoodtype(FoodType.NON_VEG);
	}

}
