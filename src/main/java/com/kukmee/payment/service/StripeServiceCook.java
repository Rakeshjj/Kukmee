package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.cookbookings.CookServiceBooking;
import com.kukmee.cookbookings.CookServiceBookingRepository;
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
	private CookServiceBookingRepository cookServiceBookingRepository;

	@Autowired
	private CookBookingPaymentRepo cookBookingPaymentRepo;

	public StripeResponse checkOutCookBooking(Long id) {
		CookServiceBooking chefBooking = cookServiceBookingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + id));

		Long totalAmountInCents = (long) (chefBooking.getGrandTotal() * 100); 

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Cook Booking").build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://127.0.0.1:5500/index-food.html?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://127.0.0.1:5500/payment-failed.html").addLineItem(lineItem).build();

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
		payment.setAmount(chefBooking.getGrandTotal());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setCookServiceBooking(chefBooking);
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
