package com.kukmee.kukmart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class KukmartOrderService {

	@Autowired
	private KukmartOrderRepository orderRepository;

	public KukmartOrder createOrder(KukmartOrder order) {

		double subtotal = order.getTotalAmount();

		double gst = subtotal * 0.18;

		double totalAmount = gst + subtotal;

		order.setStatus("CONFIRMED");
		order.setGst(gst);
		order.setTotalAmount(totalAmount);
		return orderRepository.save(order);
	}

	public List<KukmartOrder> getOrdersByCustomer(String username) {
		return orderRepository.findByCustomerUsername(username);
	}

	public KukmartOrder getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
	}

	public KukmartOrder updateOrderStatus(Long id, String status) {
		KukmartOrder order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(status);
		return orderRepository.save(order);
	}

	public KukmartOrder cancelOrder(Long id) {
		KukmartOrder order = getOrderById(id);
		if (!"CONFIRMED".equalsIgnoreCase(order.getStatus())) {
			throw new IllegalArgumentException("Only confiirmed orders can be canceled.");
		}
		order.setStatus("CANCELED");
		return orderRepository.save(order);

	}

	public List<KukmartOrder> getAll() {
		return orderRepository.findAll();
	}

	public void delete(Long id) {
		if (!orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND:" + id);
		}
		orderRepository.deleteById(id);
	}
}
