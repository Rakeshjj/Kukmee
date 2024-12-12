package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.cook.DomesticCookBooking;
import com.kukmee.cook.DomesticCookBookingRepository;
import com.kukmee.cook.MonthlyCookBooking;
import com.kukmee.cook.MonthlyCookBookingRepository;
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
	private CookBookingPaymentRepo cookBookingPaymentRepo;

	@Autowired
	private DomesticCookBookingRepository domesticCookBookingRepository;

	@Autowired
	private MonthlyCookBookingRepository monthlyCookBookingRepository;

	public StripeResponse checkOutCookBooking(Long cookId) {
		DomesticCookBooking cateringBooking = domesticCookBookingRepository.findById(cookId)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + cookId));

		Long totalAmountInCents = (long) (cateringBooking.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + cookId).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8081/payment/v1/cooksuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/payment/v1/cookcancel").addLineItem(lineItem).build();

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
		payment.setAmount(cateringBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setCookBooking(cateringBooking);
		cookBookingPaymentRepo.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}
	
	public StripeResponse checkOutMonthlyCookBooking(Long monthlyCookId) {
		
		MonthlyCookBooking cookBooking = monthlyCookBookingRepository.findById(monthlyCookId)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + monthlyCookId));

		Long totalAmountInCents = (long) (cookBooking.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + monthlyCookId).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8081/payment/v1/monthlycooksuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/payment/v1/monthlycookcancel").addLineItem(lineItem).build();

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
		payment.setAmount(cookBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setMonthlyCookBooking(cookBooking);
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
