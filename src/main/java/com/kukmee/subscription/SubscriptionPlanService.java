package com.kukmee.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

@Service
public class SubscriptionPlanService {

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private static final double DIETARY_MEAL_COST = 60.0;

	private static final double VRAD_MEAL_COST = 50.0;

	private static final double CATERING_MEAL_COST = 150.0;

	public SubscriptionPlan saveSubscriptionPlan(Long customerid, SubscriptionPlan subscriptionPlan) {

		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

		subscriptionPlan.setCustomer(customer);

		double calculatedCost = calculateCost(subscriptionPlan);
		subscriptionPlan.setCost(calculatedCost);
		
		return subscriptionPlanRepository.save(subscriptionPlan);
	}

	public double calculateCost(SubscriptionPlan subscriptionPlan) {

		double cost = 0.0;

		switch (subscriptionPlan.getPlanType()) {
		case "dietary":
			cost = DIETARY_MEAL_COST * subscriptionPlan.getDuration();
			break;
		case "vrad":
			cost = VRAD_MEAL_COST * subscriptionPlan.getDuration();
			break;
		case "catering":
			cost = CATERING_MEAL_COST * subscriptionPlan.getDuration();
			break;
		default:
			throw new IllegalArgumentException("Unknown plan Type: " + subscriptionPlan.getPlanType());
		}

		return cost;
	}

	public List<SubscriptionPlan> getAllSubscriptionPlans() {
		return subscriptionPlanRepository.findAll();
	}

	public SubscriptionPlan getSubscriptionPlanById(Long id) {
		return subscriptionPlanRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Subscription Plan not found for id: " + id));
	}

	public List<SubscriptionPlan> getSubscriptionByPlanType(String planType) {
		return (List<SubscriptionPlan>) subscriptionPlanRepository.findActiveSubscriptions(planType);
	}
}
