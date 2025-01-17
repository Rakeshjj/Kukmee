package com.kukmee.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kukmee.foodorders.Order;
import com.kukmee.foodorders.OrderRepository;
import com.kukmee.payment.OrderPayment;
import com.kukmee.payment.StripeResponse;
import com.kukmee.payment.repo.OrderPaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeServiceOrder {

	@Value("${stripe.secretKey}")
	private String secretKey;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderPaymentRepository orderPaymentRepository;

	public StripeResponse checkOutOrder(Long orderid) {

		Order order = orderRepository.findById(orderid)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderid));

		BigDecimal totalAmount = order.getTotalamount();
		BigDecimal totalAmountInCents = totalAmount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);


		Stripe.apiKey = secretKey;

		// Create the Stripe session for checkout
		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName("Order").build();

		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency(order.getTotalamount() != null ? "USD" : "INR").setUnitAmountDecimal(totalAmountInCents)
				.setProductData(productData).build();

		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder().setQuantity(1L)
				.setPriceData(priceData).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8082/payment/v1/success?session_id={CHECKOUT_SESSION_ID}")
				.setCancelUrl("http://localhost:8082/payment/v1/cancel").addLineItem(lineItem).build();

		Session session;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			e.printStackTrace(); // Log the error for debugging
			return StripeResponse.builder().status("FAILED").message("Error creating Stripe session: " + e.getMessage())
					.build();
		}

		// Save payment details
		OrderPayment payment = new OrderPayment();
		payment.setSessionId(session.getId());
		payment.setAmount(order.getTotalamount().longValue());
		payment.setCurrency("INR");
		payment.setStatus("PENDING");
		payment.setOrder(order);
		orderPaymentRepository.save(payment);

		return StripeResponse.builder().status("SUCCESS").message("Payment session created").sessionId(session.getId())
				.sessionUrl(session.getUrl()).build();
	}

	public String handlePaymentSuccess(String sessionId) {
		System.out.println("Session ID received: " + sessionId);
		OrderPayment payment = orderPaymentRepository.findBySessionId(sessionId);
		if (payment == null) {
			System.out.println("No payment found for session ID: " + sessionId);
			return "Payment not found";
		}
		payment.setStatus("SUCCESS");
		orderPaymentRepository.save(payment);
		return "Payment successful!";
	}

	public String handlePaymentFailure(String sessionId) {
		OrderPayment payment = orderPaymentRepository.findBySessionId(sessionId);
		if (payment != null) {
			payment.setStatus("FAILED");
			orderPaymentRepository.save(payment);
			return "Payment failed!";
		}
		return "Payment not found";
	}

}
