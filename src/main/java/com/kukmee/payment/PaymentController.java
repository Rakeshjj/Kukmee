package com.kukmee.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.service.StripeServiceCatering;
import com.kukmee.payment.service.StripeServiceChef;
import com.kukmee.payment.service.StripeServiceOrder;

@RestController
@RequestMapping("/payment/v1")
public class PaymentController {

	@Autowired
	private StripeServiceOrder stripeServiceOrder;

	@Autowired
	private StripeServiceCatering stripeServiceCatering;

	@Autowired
	private StripeServiceChef stripeServiceChef;

	@PostMapping("/order")
	public ResponseEntity<StripeResponse> checkoutOrder(@RequestParam Long orderid) {
		StripeResponse response = stripeServiceOrder.checkOutOrder(orderid);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/chef")
	public ResponseEntity<StripeResponse> checkoutBookingCreation(@RequestParam String chefBookingId) {
		StripeResponse stripeResponse = stripeServiceChef.checkOutChefBooking(chefBookingId);
		return ResponseEntity.ok(stripeResponse);
	}

	@PostMapping("/chefMul")
	public ResponseEntity<StripeResponse> checkoutBookingCreationMultiple(@RequestParam String chefDayId) {
		StripeResponse stripeResponse = stripeServiceChef.checkOutChefBookingMultiple(chefDayId);
		return ResponseEntity.ok(stripeResponse);
	}

	@PostMapping("/chefMonthly")
	public ResponseEntity<StripeResponse> checkoutBookingCreationMonthly(@RequestParam String id) {
		StripeResponse stripeResponse = stripeServiceChef.checkOutChefBookingMonthly(id);
		return ResponseEntity.ok(stripeResponse);
	}

	@PostMapping("/catering")
	public ResponseEntity<StripeResponse> checkoutCateringBooking(@RequestParam Long cateringId) {
		StripeResponse stripeResponse = stripeServiceCatering.checkOutCateringBooking(cateringId);
		return ResponseEntity.ok(stripeResponse);
	}

	@GetMapping("/success")
	public ResponseEntity<String> handlePaymentSuccessOrder(@RequestParam("session_id") String sessionId) {
		String response = stripeServiceOrder.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cancel")
	public ResponseEntity<String> handlePaymentCancelorder(@RequestParam String sessionId) {
		String response = stripeServiceOrder.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/chefsuccess")
	public ResponseEntity<String> handlePaymentSuccessChef(@RequestParam("session_id") String sessionId) {
		String response = stripeServiceChef.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/chefcancel")
	public ResponseEntity<String> handlePaymentCancelChef(@RequestParam String sessionId) {
		String response = stripeServiceChef.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cateringsuccess")
	public ResponseEntity<String> handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
		String response = stripeServiceCatering.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cateringcancel")
	public ResponseEntity<String> handlePaymentCancel(@RequestParam String sessionId) {
		String response = stripeServiceCatering.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}
}
