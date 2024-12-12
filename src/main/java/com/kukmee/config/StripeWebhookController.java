package com.kukmee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.ChefBookingPayment;
import com.kukmee.payment.repo.ChefBookingPaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/webhook/stripe")
public class StripeWebhookController {

    private static final String endpointSecret = "whsec_dbf34e66483b7bd3295c16eb47e6610b1c00a144ed40ecf1532d96ee6af577e0";

    @Autowired
    private ChefBookingPaymentRepository chefBookingPaymentRepository;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        try {
            if (sigHeader == null || sigHeader.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Stripe-Signature header");
            }

            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("Stripe Event Received: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String sessionId = session.getId();
                    handlePaymentSuccess(sessionId);
                }
            } else if ("payment_intent.payment_failed".equals(event.getType())) {
                String sessionId = extractSessionIdFromEvent(event);
                handlePaymentFailure(sessionId);
            }

            return ResponseEntity.ok("Event processed");
        } catch (SignatureVerificationException e) {
            System.err.println("Invalid signature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook processing error");
        }
    }

    private void handlePaymentSuccess(String sessionId) {
        ChefBookingPayment payment = chefBookingPaymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus("SUCCESS");
            chefBookingPaymentRepository.save(payment);
            System.out.println("Payment status updated to SUCCESS for session ID: " + sessionId);
        } else {
            System.err.println("No payment record found for session ID: " + sessionId);
        }
    }

    private void handlePaymentFailure(String sessionId) {
        ChefBookingPayment payment = chefBookingPaymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus("FAILED");
            chefBookingPaymentRepository.save(payment);
            System.out.println("Payment status updated to FAILED for session ID: " + sessionId);
        } else {
            System.err.println("No payment record found for session ID: " + sessionId);
        }
    }

    private String extractSessionIdFromEvent(Event event) {
        // Attempt to extract session ID (if included in event)
        Object dataObject = event.getDataObjectDeserializer().getObject().orElse(null);
        if (dataObject instanceof Session) {
            return ((Session) dataObject).getId();
        }
        return null;
    }
}
