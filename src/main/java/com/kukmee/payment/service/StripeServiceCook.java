package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.cook.CookBooking;
import com.kukmee.cook.repo.CookBookingRepo;
import com.kukmee.payment.CateringBookingPayment;
import com.kukmee.payment.CookBookingPayment;
import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.repo.CookBookingPaymentRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeServiceCook {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private CookBookingRepo cookBookingRepo;

	@Autowired
	private CookBookingPaymentRepo cookBookingPaymentRepo;

	public StripeResponse checkOutCookBooking(String cookBookingId) {
		CookBooking chefBooking = cookBookingRepo.findById(cookBookingId)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + cookBookingId));

		Long totalAmountInCents = (long) (chefBooking.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + cookBookingId).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8082/payment/v1/cooksuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8082/payment/v1/cookcancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		CookBookingPayment payment = new CookBookingPayment();
		payment.setSessionId(session.getId());
		payment.setAmount(chefBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setCookBookingOneMeal(chefBooking);
		payment.setPaymentId(session.getPaymentIntent());
		cookBookingPaymentRepo.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}
	
	public String handlePaymentSuccess(String sessionId) {
		System.out.println("Session ID received: " + sessionId);
		CookBookingPayment payment = cookBookingPaymentRepo.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		cookBookingPaymentRepo.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {
		CookBookingPayment payment = cookBookingPaymentRepo.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			cookBookingPaymentRepo.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}


}
