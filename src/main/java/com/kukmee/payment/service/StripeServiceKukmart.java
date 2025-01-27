package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.kukmart.KukmartOrder;
import com.kukmee.kukmart.KukmartOrderRepository;
import com.kukmee.payment.KukmartPayment;
import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.repo.KukmartPaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeServiceKukmart {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private KukmartPaymentRepository kukmartPaymentRepository;

	@Autowired
	private KukmartOrderRepository kukmartOrderRepository;

	public StripeResponse checkOutKukmart(Long id) {

		long startTime = System.currentTimeMillis();

		KukmartOrder kukmartOrder = kukmartOrderRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("kukmart order not found with ID: " + id));

		Long totalAmountInCents = (long) (kukmartOrder.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Kukmart Order").build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://127.0.0.1:5500/index-food.html?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://127.0.0.1:5500/payment-failed.html").addLineItem(lineItem).build();

		Session session;
		try {
			long sessionStartTime = System.currentTimeMillis();
			session = Session.create(params);
			long sessionEndTime = System.currentTimeMillis();
			System.out.println("Time taken for Stripe Session.create: " + (sessionEndTime - sessionStartTime) + "ms");

			KukmartPayment payment = new KukmartPayment();
			payment.setSessionId(session.getId());
			payment.setAmount(kukmartOrder.getTotalAmount());
			payment.setCurrency("INR");
			payment.setStatus("PENDING");
			payment.setKukmartOrder(kukmartOrder);

			long endTime = System.currentTimeMillis();
			System.out.println("Total Time taken: " + (endTime - startTime) + "ms");

			kukmartPaymentRepository.save(payment);

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

		KukmartPayment payment = kukmartPaymentRepository.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		kukmartPaymentRepository.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {

		KukmartPayment payment = kukmartPaymentRepository.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			kukmartPaymentRepository.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}

}
