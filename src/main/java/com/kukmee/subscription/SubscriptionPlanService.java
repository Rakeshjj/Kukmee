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

	public SubscriptionPlan saveSubscriptionPlan(Long customerid, SubscriptionPlan subscriptionPlan) {

		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

		subscriptionPlan.setCustomer(customer);

		double calculatedTotal = subscriptionPlan.getQuantity() * subscriptionPlan.getTotalAmount();
		subscriptionPlan.setTotalAmount(calculatedTotal);

		return subscriptionPlanRepository.save(subscriptionPlan);
	}

	public List<SubscriptionPlan> getAllSubscriptionPlans() {
		return subscriptionPlanRepository.findAll();
	}

	public SubscriptionPlan getSubscriptionPlanById(Long id) {
		return subscriptionPlanRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Subscription Plan not found for id: " + id));
	}
}
