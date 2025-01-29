package com.kukmee.vratmeals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vratsubscription")
public class VratMealSubscriptionController {

	@Autowired
	private VratMealSubscriptionService service;

	@PostMapping("/create")
	public ResponseEntity<?> createSubscription(@RequestBody VratMealSubscription subscription) {
		service.saveSubscription(subscription);
		return ResponseEntity.ok("Subscription successfully!!");
	}
}
