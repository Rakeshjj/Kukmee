package com.kukmee.foodorders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestBody Order order, @RequestParam Long customerid) {
		try {

			System.out.println("Received Order: " + order); // Debugging

			Order savedOrder = orderService.saveOrder(customerid, order);
			ResponseEntity<?> paymentResponse = paymentController.checkoutOrder(order.getOrderId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getByID(@PathVariable Long orderId) {
		Order order = orderService.getOrderById(orderId);
		return ResponseEntity.ok(order);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<?> deleteById(@RequestParam Long orderId) {
		orderService.deleteOrder(orderId);
		return ResponseEntity.ok("Order deleted successfully...");
	}
}
