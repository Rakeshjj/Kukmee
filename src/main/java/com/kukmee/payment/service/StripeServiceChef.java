package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.chef.ChefBooking;
import com.kukmee.chef.repo.ChefBookingRepository;
import com.kukmee.payment.ChefBookingPayment;
import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.repo.ChefBookingPaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeServiceChef {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private ChefBookingRepository chefBookingRepository;

	@Autowired
	private ChefBookingPaymentRepository chefBookingPaymentRepoitory;

	public StripeResponse checkOutChefBooking(Long chefBookingId) {
		ChefBooking chefBooking = chefBookingRepository.findById(chefBookingId)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + chefBookingId));

		Long totalAmountInCents = (long) (chefBooking.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + chefBookingId).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8081/payment/v1/chefsuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/payment/v1/chefcancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		ChefBookingPayment payment = new ChefBookingPayment();
		payment.setSessionId(session.getId());
		payment.setAmount(chefBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setChefBooking(chefBooking);
		chefBookingPaymentRepoitory.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

	public String handlePaymentSuccess(String sessionId) {
		System.out.println("Session ID received: " + sessionId);
		ChefBookingPayment payment = chefBookingPaymentRepoitory.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		chefBookingPaymentRepoitory.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {
		ChefBookingPayment payment = chefBookingPaymentRepoitory.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			chefBookingPaymentRepoitory.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}

}
