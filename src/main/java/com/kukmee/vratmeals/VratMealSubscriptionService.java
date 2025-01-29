package com.kukmee.vratmeals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

@Service
public class VratMealSubscriptionService {

	@Autowired
	private VratMealSubscriptionRepository repository;
	
	@Autowired
	private CustomerRepository customerRepository;

	public VratMealSubscription saveSubscription(VratMealSubscription subscription, Long customerid) {
		
		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

		subscription.setCustomer(customer);
		return repository.save(subscription);
	}

	public List<VratMealSubscription> getAll() {
		return repository.findAll();
	}
}
