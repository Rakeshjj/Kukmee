package com.kukmee.kukmart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/kukmartorders")
public class KukmartOrderController {

	@Autowired
	private KukmartOrderService orderService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create/{customerid}")
	public ResponseEntity<?> createKukmartOrder(@Valid @RequestBody KukmartOrder order, @PathVariable Long customerid) {

		try {
			KukmartOrder savedOrder = orderService.createOrder(order, customerid);
			ResponseEntity<?> paymentResponse = paymentController.checkoutKukmart(savedOrder.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

//	@PreAuthorize("hasRole('CUSTOMER')")
//	@GetMapping("/customer/{username}")
//	public List<KukmartOrder> getOrdersByCustomer(@PathVariable String username) {
//		return orderService.getOrdersByCustomer(username);
//	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@GetMapping("/{id}")
	public KukmartOrder getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/status")
	public KukmartOrder updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
		return orderService.updateOrderStatus(id, status);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/{id}/cancel")
	public KukmartOrder cancelOrder(@PathVariable Long id) {
		return orderService.cancelOrder(id);
	}

//	@PreAuthorize("hasRole('CUSTOMER')")
//	@PostMapping("/refill")
//	public ResponseEntity<?> refillOrder(@RequestParam Long id) {
//		try {
//			KukmartOrder newOrder = orderService.refillOrder(id);
//			return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Refill order failed: " + e.getMessage());
//		}
//	}

}
