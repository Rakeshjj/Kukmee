package com.kukmee.kukmart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.repository.CustomerRepository;

@Service
public class KukmartOrderService {

	@Autowired
	private KukmartOrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public KukmartOrder createOrder(KukmartOrder order, Long customerid) {

		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

		order.setCustomer(customer);

		List<KukmartOrderItem> items = order.getItems();
		double subtotal = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
		double gst = subtotal * 0.18;
		double totalAmount = gst + subtotal;

		order.setStatus("CONFIRMED");
		order.setGst(gst);
		order.setTotalAmount(totalAmount);

		return orderRepository.save(order);
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

//	public KukmartOrder refillOrder(Long id) {
//
//		KukmartOrder previousOrder = orderRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
//
//		KukmartOrder newOrder = new KukmartOrder();
//		newOrder.setCustomerUsername(previousOrder.getCustomerUsername());
//
//		List<KukmartOrderItem> copiedItems = previousOrder.getItems().stream().map(item -> {
//			KukmartOrderItem newItem = new KukmartOrderItem();
//			newItem.setItemName(item.getItemName());
//			newItem.setQuantity(item.getQuantity());
//			newItem.setPrice(item.getPrice());
//			return newItem;
//		}).toList();
//		newOrder.setItems(copiedItems);
//
//		double subtotal = copiedItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
//		double gst = subtotal * 0.18;
//		newOrder.setGst(gst);
//		newOrder.setTotalAmount(subtotal + gst);
//
//		DeliveryDetails previousDelivery = previousOrder.getDeliveryDetails();
//		if (previousDelivery != null) {
//			DeliveryDetails newDeliveryDetails = new DeliveryDetails();
//			newDeliveryDetails.setDeliveryAddress(previousDelivery.getDeliveryAddress());
//			newDeliveryDetails.setDeliveryDate(previousDelivery.getDeliveryDate());
//			newDeliveryDetails.setDeliveryTimeSlot(previousDelivery.getDeliveryTimeSlot());
//			newOrder.setDeliveryDetails(newDeliveryDetails); 
//		} else {
//			throw new IllegalArgumentException("Delivery details missing in previous order.");
//		}
//
//		newOrder.setStatus("PENDING");
//
//		return orderRepository.save(newOrder);
//	}

}
