package com.kukmee.subscription;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin("*")
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private PlanRepository planRepository;

	@PreAuthorize("hasRole('CUSTOMER')")	
	@PostMapping("/create")
	public ResponseEntity<?> createSubscription(@RequestParam String username, @RequestParam String planName,
			@RequestParam String startDate, @RequestParam String endDate) {

		LocalDate parsedStartDate = LocalDate.parse(startDate);
		LocalDate parsedEndDate = LocalDate.parse(endDate);

		Subscription subscription = subscriptionService.createSubscription(username, planName, parsedStartDate,
				parsedEndDate);
		return ResponseEntity.ok(subscription);
	}

	@GetMapping("/plans")
	public ResponseEntity<List<Plan>> getAllPlans() {
		return ResponseEntity.ok(planRepository.findAll());
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/add-plan")
	public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
		return ResponseEntity.ok(planRepository.save(plan));
	}
}
