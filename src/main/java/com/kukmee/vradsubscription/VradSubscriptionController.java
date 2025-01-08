package com.kukmee.vradsubscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class VradSubscriptionController {

	@Autowired
	private VradSubscriptionService subscriptionService;

	@GetMapping
	public List<VradSubscriptionPlan> getAllSubscriptionPlans() {
		return subscriptionService.getAllSubscriptionPlans();
	}

	@GetMapping("/{id}")
	public VradSubscriptionPlan getSubscriptionPlanById(@PathVariable Long id) {
		return subscriptionService.getSubscriptionPlanById(id);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> addSubscriptionPlan(@RequestBody VradSubscriptionPlan plan) {
		subscriptionService.addSubscriptionPlan(plan);
		return ResponseEntity.ok("Plan created succeessfully");
	}

}
