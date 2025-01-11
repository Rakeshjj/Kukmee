package com.kukmee.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

	@Autowired
	private SubscriptionPlanService subscriptionPlanService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createSubscriptionPlan(@RequestBody SubscriptionPlan subscriptionPlan,
			@RequestParam Long customerid) {

		try {
			subscriptionPlanService.saveSubscriptionPlan(customerid, subscriptionPlan);
			ResponseEntity<?> paymentResponse = paymentController.subscriptionPlan(subscriptionPlan.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/")
	public ResponseEntity<List<SubscriptionPlan>> getAllSubscriptionPlans() {
		return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlans());
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/id/{id}")
	public ResponseEntity<SubscriptionPlan> getSubscriptionPlanById(@PathVariable Long id) {
		return ResponseEntity.ok(subscriptionPlanService.getSubscriptionPlanById(id));
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/{planType}")
	public List<SubscriptionPlan> getSubscription(@PathVariable String planType) {
		return subscriptionPlanService.getSubscriptionByPlanType(planType); // Return list
	}
}
