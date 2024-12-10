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

	@PostMapping("/order")
	public ResponseEntity<StripeResponse> checkoutOrder(@RequestParam Long orderid) {
		StripeResponse response = stripeService.checkOutOrder(orderid);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/chef")
	public ResponseEntity<StripeResponse> checkoutBookingCreation(@RequestParam Long chefBookingId) {
		StripeResponse stripeResponse = stripeService.checkOutChefBooking(chefBookingId);
		return ResponseEntity.ok(stripeResponse);
	}

	@PostMapping("/catering")
	public ResponseEntity<StripeResponse> checkoutCateringBooking(@RequestParam Long cateringId) {
		StripeResponse stripeResponse = stripeService.checkOutCateringBooking(cateringId);
		return ResponseEntity.ok(stripeResponse);
	}

	
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
