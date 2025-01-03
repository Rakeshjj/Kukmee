package com.kukmee.kukmart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.inventory.InventoryService;

@Service
public class KukmartOrderService {

	@Autowired
	private KukmartOrderRepository orderRepository;

	@Autowired
	private InventoryService inventoryService;

	public KukmartOrder createOrder(KukmartOrder order) {

		for (KukmartOrderItem item : order.getItems()) {
			inventoryService.deductStock(item.getItemName(), item.getQuantity());
		}
		order.setStatus("Pending");
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
