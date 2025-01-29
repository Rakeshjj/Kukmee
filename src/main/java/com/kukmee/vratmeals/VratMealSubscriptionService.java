package com.kukmee.vratmeals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VratMealSubscriptionService {

	@Autowired
	private VratMealSubscriptionRepository repository;

	public VratMealSubscription saveSubscription(VratMealSubscription subscription) {
		return repository.save(subscription);
	}

	public List<VratMealSubscription> getAll() {
		return repository.findAll();
	}
}
