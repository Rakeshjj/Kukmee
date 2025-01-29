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

		long startTime = System.currentTimeMillis();

		SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + id));
		Long totalAmountInCents = (long) (subscriptionPlan.getTotalAmount() * 100);

		Stripe.apiKey = secretKey;
		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Subscription").build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://127.0.0.1:5500/index-food.html?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://127.0.0.1:5500/payment-failed.html").addLineItem(lineItem).build();

		try {
			long sessionStartTime = System.currentTimeMillis();
			Session session = Session.create(params);
			long sessionEndTime = System.currentTimeMillis();
			System.out.println("Time taken for Stripe Session.create: " + (sessionEndTime - sessionStartTime) + "ms");

			SubscriptionPayment payment = new SubscriptionPayment();
			payment.setSessionId(session.getId());
			payment.setAmount(subscriptionPlan.getTotalAmount());
			payment.setCurrency("INR");
			payment.setStatus("PENDING");
			payment.setSubscriptionPlan(subscriptionPlan);
			subscriptionPaymentRepo.save(payment);

			long endTime = System.currentTimeMillis();
			System.out.println("Total time taken: " + (endTime - startTime) + "ms");

			return StripeResponse.builder().status("SUCCESS").message("Payment session created")
					.sessionId(session.getId()).sessionUrl(session.getUrl()).build();
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}
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
