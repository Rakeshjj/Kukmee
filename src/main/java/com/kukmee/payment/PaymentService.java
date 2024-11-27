package com.kukmee.payment;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.orders.Order;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {
	
	@Autowired
    private BillRepository billRepository;

    public PaymentIntent createPaymentIntent(Double amount) throws Exception {
    	
    	 Stripe.apiKey = "sk_test_51Q3GSrJY7aClXix3kYVQWHgqd0wFfSBz3jpaBa1R0aHNt1cpVAyXdb3aUD2Tceem0wEi71Q8fDi44jvfIcr6QDfa00YwFAv4j3";
        // Convert the amount to the smallest currency unit (e.g., cents for USD)
        long amountInCents = (long) (amount * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("inr")  // Change to your desired currency
                .build();

        // Create the payment intent using Stripe's API
        return PaymentIntent.create(params);
    }
    
    public void saveBill(Order order, PaymentIntent paymentIntent) {
        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setPaymentId(paymentIntent.getId());
        bill.setTotalAmount(order.getTotalamount());
        bill.setCurrency(paymentIntent.getCurrency());
        bill.setStatus(paymentIntent.getStatus());
        bill.setPaymentDate(new Date());

        billRepository.save(bill);
    }
}
