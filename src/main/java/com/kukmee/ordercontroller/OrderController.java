package com.kukmee.ordercontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.orders.Order;
import com.kukmee.orders.OrderItem;
import com.kukmee.orders.OrderResponseDTO;
import com.kukmee.orderservice.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody List<OrderItem> orderItems,
			@RequestParam Long customerid) {
		Map<String, Object> response = new HashMap<>();
		try {
			Order order = orderService.createOrder(customerid, orderItems);

			response.put("message", "Order created successfully!");
			response.put("orderid", order.getOrderid());

			// Return response with status 200 OK
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Handle failure and return error message
			response.put("error", "Order creation failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

//	@PreAuthorize("hasRole('CUSTOMER')")
//	@PostMapping("/create")
//	public ResponseEntity<?> createOrder(@RequestParam Long customerid, @RequestBody List<OrderItem> orderitems) {
//		System.out.println("Incoming Order Items: " + orderitems);
//		Order order = orderService.createOrder(customerid, orderitems);
//		return ResponseEntity.ok("Order created successfully..");
//	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderById(@PathVariable Long orderid) {
		Order order = orderService.getOrderById(orderid);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/all")
	public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();

		List<OrderResponseDTO> orderDTOs = orders.stream().map(orderService::convertToDTO) // Call the service's method
																							// to convert to DTO
				.collect(Collectors.toList());

		return ResponseEntity.ok(orderDTOs);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateOrder(@RequestParam Long orderid, @RequestBody Order order) {

		Order existingOrder = orderService.getOrderById(orderid);

		if (order.getStatus() != null) {
			existingOrder.setStatus(order.getStatus());
		}

		Order orderUpdate = orderService.updateOrder(orderid, order);
		return ResponseEntity.ok(orderUpdate);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{orderid}")
	public ResponseEntity<?> deleteOrder(@PathVariable Long orderid) {
		orderService.deleteOrder(orderid);
		return ResponseEntity.ok("Order deleted successfully");
	}

	@PutMapping("/cancel")
	public ResponseEntity<?> cancelOrder(@RequestParam Long orderid) {
		try {
			Order cancelOrder = orderService.cancelOrder(orderid);
			return ResponseEntity.ok("Order cancelled successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/canceled")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Order>> getAllCancelOrders() {
		List<Order> cancelOrders = orderService.getAllCancelOrder();
		return ResponseEntity.ok(cancelOrders);
	}

	@GetMapping("/confirmed")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Order>> getAllConfirmOrders() {
		List<Order> confirmOrders = orderService.getAllConfirmlOrder();
		return ResponseEntity.ok(confirmOrders);
	}

}
