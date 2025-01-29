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
import com.kukmee.payment.service.StripeServiceCook;
import com.kukmee.payment.service.StripeServiceKukmart;
import com.kukmee.payment.service.StripeServiceOrder;
import com.kukmee.payment.service.SubscriptionPaymentService;
import com.kukmee.payment.service.VratMelaspaymentService;
import com.kukmee.vratmeals.VratMealSubscriptionService;

@RestController
@RequestMapping("/payment/v1")
public class PaymentController {

	@Autowired
	private StripeServiceOrder stripeServiceOrder;

	@Autowired
	private StripeServiceCatering stripeServiceCatering;

	@Autowired
	private StripeServiceChef stripeServiceChef;

	@Autowired
	private StripeServiceCook stripeServiceCook;

	@Autowired
	private SubscriptionPaymentService subscriptionPaymentService;

	@Autowired
	private StripeServiceKukmart stripeServiceKukmart;

	@Autowired
	private VratMelaspaymentService vratMealSubscriptionService;

	@PostMapping("/vrat")
	public ResponseEntity<StripeResponse> vratSubscriptionPlan(@RequestParam Long id) {
		StripeResponse response = vratMealSubscriptionService.vratSubscriptionPlan(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/order")
	public ResponseEntity<StripeResponse> checkoutOrder(@RequestParam Long orderid) {
		StripeResponse response = stripeServiceOrder.checkOutOrder(orderid);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/chef")
	public ResponseEntity<StripeResponse> checkoutBookingChef(@RequestParam Long id) {
		StripeResponse stripeResponse = stripeServiceChef.checkOutChefBooking(id);
		return ResponseEntity.ok(stripeResponse);
	}

	@PostMapping("/cook")
	public ResponseEntity<StripeResponse> checkoutBookingCook(@RequestParam Long id) {
		StripeResponse stripeResponse = stripeServiceCook.checkOutCookBooking(id);
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

	@GetMapping("/cooksuccess")
	public ResponseEntity<String> handlePaymentSuccessCook(@RequestParam("session_id") String sessionId) {
		String response = stripeServiceCook.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/cookcancel")
	public ResponseEntity<String> handlePaymentCancelCook(@RequestParam String sessionId) {
		String response = stripeServiceCook.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/subscription")
	public ResponseEntity<StripeResponse> subscriptionPlan(@RequestParam Long id) {
		StripeResponse response = subscriptionPaymentService.subscriptionPlan(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/subscriptionsuccess")
	public ResponseEntity<String> handlePaymentSuccessSubscription(@RequestParam("session_id") String sessionId) {
		String response = subscriptionPaymentService.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/subscriptioncancel")
	public ResponseEntity<String> handlePaymentCancelSubscription(@RequestParam String sessionId) {
		String response = subscriptionPaymentService.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/kukmart")
	public ResponseEntity<StripeResponse> checkoutKukmart(@RequestParam Long id) {
		StripeResponse response = stripeServiceKukmart.checkOutKukmart(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/kukmartsuccess")
	public ResponseEntity<String> handlePaymentSuccessKukmart(@RequestParam("session_id") String sessionId) {
		String response = stripeServiceKukmart.handlePaymentSuccess(sessionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/kukmartcancel")
	public ResponseEntity<String> handlePaymentCancelKukmart(@RequestParam String sessionId) {
		String response = stripeServiceKukmart.handlePaymentFailure(sessionId);
		return ResponseEntity.ok(response);
	}
}
