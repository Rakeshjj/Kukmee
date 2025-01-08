package com.kukmee.vradsubscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VradSubscriptionService {

	@Autowired
	private VradSubscriptionRepository subscriptionPlanRepository;

	public VradSubscriptionPlan addSubscriptionPlan(VradSubscriptionPlan vradSubscriptionPlan) {

		List<VradFoodItem> foodItems = vradSubscriptionPlan.getFoodItems();

		for (VradFoodItem foodItem : foodItems) {
			foodItem.setSubscriptionPlan(vradSubscriptionPlan);
		}

		return subscriptionPlanRepository.save(vradSubscriptionPlan);
	}

	public List<VradSubscriptionPlan> getAllSubscriptionPlans() {
		return subscriptionPlanRepository.findAll();
	}

	public VradSubscriptionPlan getSubscriptionPlanById(Long id) {
		return subscriptionPlanRepository.findById(id).orElse(null);
	}
}
