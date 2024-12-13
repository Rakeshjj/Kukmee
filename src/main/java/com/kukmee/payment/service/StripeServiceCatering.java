package com.kukmee.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.catering.CateringBooking;
import com.kukmee.catering.CateringBookingRepository;
import com.kukmee.payment.CateringBookingPayment;
import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.repo.CateringPaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeServiceCatering {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private CateringPaymentRepository cateringPaymentRepository;

	@Autowired
	private CateringBookingRepository cateringBookingRepository;

	public StripeResponse checkOutCateringBooking(Long cateringId) {
		CateringBooking cateringBooking = cateringBookingRepository.findById(cateringId)
				.orElseThrow(() -> new RuntimeException("Chef booking not found with ID: " + cateringId));

		Long totalAmountInCents = (long) (cateringBooking.getTotalAmount() * 100); // Convert to cents

		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Chef Booking # " + cateringId).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("INR").setUnitAmount(totalAmountInCents).setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8082/payment/v1/cateringsuccess?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8082/payment/v1/cateringcancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		CateringBookingPayment payment = new CateringBookingPayment();
		payment.setSessionId(session.getId());
		payment.setAmount(cateringBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setCateringBooking(cateringBooking);
		cateringPaymentRepository.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

	public String handlePaymentSuccess(String sessionId) {
		System.out.println("Session ID received: " + sessionId);
		CateringBookingPayment payment = cateringPaymentRepository.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		cateringPaymentRepository.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {
		CateringBookingPayment payment = cateringPaymentRepository.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			cateringPaymentRepository.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}

}
