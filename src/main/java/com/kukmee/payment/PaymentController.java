package com.kukmee.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.orders.Order;
import com.kukmee.orderservice.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private BillRepository billRepository;

    @PostMapping("/create/{orderid}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable Long orderid) throws Exception {
        try {
            Order order = orderService.getOrderById(orderid);

            PaymentIntent paymentIntent = paymentService.createPaymentIntent(order.getTotalamount());
            paymentService.saveBill(order, paymentIntent);

            // Send the clientSecret to the frontend
            return ResponseEntity.ok(new PaymentIntentResponse(paymentIntent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment intent: " + e.getMessage());
        }
    }
    
    @GetMapping("/bill/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));
        return ResponseEntity.ok(bill);
    }


    // Handle OPTIONS requests explicitly for preflight CORS checks
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/create/{orderId}")
    public ResponseEntity<Void> handlePreflight() {
        return ResponseEntity.ok().build();  // Respond with status 200 for preflight requests
    }
}

// Response class to send clientSecret to the frontend
class PaymentIntentResponse {
    private String clientSecret;

    public PaymentIntentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
