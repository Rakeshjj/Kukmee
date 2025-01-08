package com.kukmee.dietarymeals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class MealService {

	@Autowired
	private MealSubscriptionRepository mealRepository;

	public MealSubscription createSubscription(MealSubscription mealSubscription) {

		double price = 0;

		if ("7 Days".equals(mealSubscription.getMealPlan())) {
			price = 50.00;
		} else if ("14 Days".equals(mealSubscription.getMealPlan())) {
			price = 95.00;
		} else if ("30 Days".equals(mealSubscription.getMealPlan())) {
			price = 180.00;
		}

		mealSubscription.setTotalAmount(price);

		return mealRepository.save(mealSubscription);
	}

	public List<MealSubscription> getMealOption() {
		return mealRepository.findAll();
	}

	public MealSubscription getById(Long id) {
		return mealRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));

	}

	public void deleteById(Long id) {
		if (!mealRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND :" + id);
		}
		mealRepository.deleteById(id);
	}
}
