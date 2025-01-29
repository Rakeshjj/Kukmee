package com.kukmee.vratmeals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VratMealSubscriptionService {

	@Autowired
	private VratMealSubscriptionRepository repository;

	public VratMealSubscription saveSubscription(VratMealSubscription subscription) {
		return repository.save(subscription);
	}
}
