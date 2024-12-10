package com.kukmee.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.catering.CateringBooking;
import com.kukmee.catering.CateringBookingRepository;
import com.kukmee.chef.ChefBooking;
import com.kukmee.chef.ChefBookingRepository;
import com.kukmee.orders.Order;
import com.kukmee.orders.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ChefBookingRepository chefBookingRepository;

	@Autowired
	private CateringBookingRepository cateringBookingRepository;

	public StripeResponse checkOutOrder(Long orderid) {

		Order order = orderRepository.findById(orderid)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderid));

		// Calculate amount (if not already calculated)
		Long totalAmountInCents = (long) (order.getTotalamount() * 100); // Stripe expects cents

		Stripe.apiKey = secretKey;

		// Create the Stripe session for checkout
		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Order # " + order.getTotalamount()).build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency(order.getTotalamount() != null ? "USD" : "INR").setUnitAmount(totalAmountInCents)
				.setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8081/success?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/cancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace(); // Log the error for debugging
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		// Save payment details
		Payment payment = new Payment();
		payment.setSessionId(session.getId());
		payment.setAmount(order.getTotalamount().longValue());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setOrder(order);
		paymentRepository.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

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
				.setSuccessUrl("http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/payment/cancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		Payment payment = new Payment();
		payment.setSessionId(session.getId());
		payment.setAmount(chefBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setChefBooking(chefBooking);
		paymentRepository.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

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
				.setSuccessUrl("http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8081/payment/cancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace();
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		Payment payment = new Payment();
		payment.setSessionId(session.getId());
		payment.setAmount(cateringBooking.getTotalAmount());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setCateringBooking(cateringBooking);
		paymentRepository.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

	public String handlePaymentSuccess(String sessionId) {
		Payment payment = paymentRepository.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("SUCCESS");
			paymentRepository.save(payment);
			return "Payment successful!";
		}
		return "Payment not found";
	}

	public String handlePaymentFailure(String sessionId) {
		Payment payment = paymentRepository.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			paymentRepository.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}
}


