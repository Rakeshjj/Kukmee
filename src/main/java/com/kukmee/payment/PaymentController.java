package com.kukmee.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment/v1")
public class PaymentController {

	@Autowired
	private StripeService stripeService;

	@PostMapping("/checkout")
	public ResponseEntity<StripeResponse> checkoutOrder(@RequestParam Long orderId) {
		StripeResponse response = stripeService.checkOutOrder(orderId);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/success")
	public ResponseEntity<String> handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
		String response = stripeService.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cancel")
	public ResponseEntity<String> handlePaymentCancel(@RequestParam String sessionId) {
		String response = stripeService.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}
}
