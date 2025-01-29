package com.kukmee.milksubscription;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/milk-subscription")
@RequiredArgsConstructor
public class MilkSubscriptionController {

	@Autowired
	private MilkSubscriptionService milkSubscriptionService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create/{customerid}")
	public ResponseEntity<?> createSubscription(@RequestBody MilkSubscription subscription,
			@PathVariable Long customerid) {
		milkSubscriptionService.createSubscription(customerid, subscription);
		return ResponseEntity.ok("Subscription successfully!!");
	}

	@PostMapping("/pause/{subscriptionId}")
	public ResponseEntity<SubscriptionPause> pauseSubscription(@PathVariable Long subscriptionId,
			@RequestParam String pauseDate) throws MessagingException {
		LocalDate date = LocalDate.parse(pauseDate);
		return ResponseEntity.ok(milkSubscriptionService.pauseSubscription(subscriptionId, date));
	}

	@GetMapping("/all")
	public ResponseEntity<List<MilkSubscription>> getAllSubscriptions() {
		return ResponseEntity.ok(milkSubscriptionService.getAllSubscriptions());
	}
}
