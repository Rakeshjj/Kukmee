package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.SubscriptionPayment;
import com.kukmee.payment.repo.SubscriptionPaymentRepo;
import com.kukmee.subscription.SubscriptionPlan;
import com.kukmee.subscription.SubscriptionPlanRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class SubscriptionPaymentService {

	@Autowired
	private SubscriptionPaymentRepo subscriptionPaymentRepo;

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	public StripeResponse subscriptionPlan(Long id) {
		SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + id));

		Long totalAmountInCents = (long) (subscriptionPlan.getCost() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + id).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8082/payment/v1/subscriptionsuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8082/payment/v1/subscriptioncancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		SubscriptionPayment payment = new SubscriptionPayment();
		payment.setSessionId(session.getId());
		payment.setAmount(subscriptionPlan.getCost());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setSubscriptionPlan(subscriptionPlan);
		subscriptionPaymentRepo.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

	public String handlePaymentSuccess(String sessionId) {
		System.out.println("Session ID received: " + sessionId);
		SubscriptionPayment payment = subscriptionPaymentRepo.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		subscriptionPaymentRepo.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {
		SubscriptionPayment payment = subscriptionPaymentRepo.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			subscriptionPaymentRepo.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}

}
