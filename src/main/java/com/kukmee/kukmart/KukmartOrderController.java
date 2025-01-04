package com.kukmee.kukmart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kukmartorders")
public class KukmartOrderController {

	@Autowired
	private KukmartOrderService orderService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public KukmartOrder createOrder(@RequestBody KukmartOrder order) {
		return orderService.createOrder(order);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/customer/{username}")
	public List<KukmartOrder> getOrdersByCustomer(@PathVariable String username) {
		return orderService.getOrdersByCustomer(username);
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@GetMapping("/{id}")
	public KukmartOrder getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id);
	}

//	@GetMapping("/customer/{username}")
//	public Page<KukmartOrder> getOrdersByCustomer(@PathVariable String username,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
//		Pageable pageable = PageRequest.of(page, size);
//		return orderService.getOrderByCustomer(username, pageable);
//	}

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
}
