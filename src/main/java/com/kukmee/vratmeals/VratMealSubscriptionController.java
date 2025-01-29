package com.kukmee.vratmeals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/vratsubscription")
public class VratMealSubscriptionController {

	@Autowired
	private VratMealSubscriptionService service;

	@Autowired
	private PaymentController paymentController;

	@PostMapping("/create/{customerid}")
	public ResponseEntity<?> createSubscription(@RequestBody VratMealSubscription subscription,
			@PathVariable Long customerid) {

		try {
			service.saveSubscription(subscription, customerid);
			ResponseEntity<?> paymentResponse = paymentController.vratSubscriptionPlan(customerid);

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}
}
