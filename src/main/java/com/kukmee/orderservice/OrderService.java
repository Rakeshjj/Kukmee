package com.kukmee.orderservice;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.foodentity.FoodItem;
import com.kukmee.foodentity.FoodItemRepository;
import com.kukmee.orders.Order;
import com.kukmee.orders.OrderItem;
import com.kukmee.orders.OrderItemDTO;
import com.kukmee.orders.OrderRepository;
import com.kukmee.orders.OrderResponseDTO;
import com.kukmee.repository.CustomerRepository;

@Service
public class OrderService {

	private final FoodItemRepository foodItemRepository;
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;

	@Autowired
	public OrderService(FoodItemRepository foodItemRepository, OrderRepository orderRepository,
			CustomerRepository customerRepository) {
		super();
		this.foodItemRepository = foodItemRepository;
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
	}

	public Order createOrder(Long customerid, List<OrderItem> orderItems) {
		// Fetch Customer
		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerid));

		// Fetch FoodItems and validate
		for (OrderItem item : orderItems) {
			if (item.getFooditem() == null || item.getFooditem().getFoodid() == null) {
				throw new IllegalArgumentException("FoodItem ID must not be null");
			}
			FoodItem foodItem = foodItemRepository.findById(item.getFooditem().getFoodid())
					.orElseThrow(() -> new ResourceNotFoundException(
							"FoodItem not found with ID: " + item.getFooditem().getFoodid()));
			item.setFooditem(foodItem);
		}

		// Calculate total amount
		Double totalAmount = orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		// Create and save the order
		Order order = new Order();
		order.setCustomer(customer);
		order.setOrderdate(new Date());
		order.setStatus("PENDING");
		order.setOrderitems(orderItems);
		order.setTotalamount(totalAmount);

		orderItems.forEach(item -> item.setOrder(order));
		order.setOrderitems(orderItems);

		Order saveOrder = orderRepository.save(order);
		saveOrder.setStatus("CONFIRMED");
		return orderRepository.save(saveOrder);
	}

	public Order getOrderById(Long orderid) {
		return orderRepository.findById(orderid)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID:" + orderid));
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public OrderResponseDTO convertToDTO(Order order) {
		OrderResponseDTO orderDTO = new OrderResponseDTO();
		orderDTO.setOrderid(order.getId());
		orderDTO.setCustomer(order.getCustomer());
		orderDTO.setOrderdate(order.getOrderdate());
		orderDTO.setStatus(order.getStatus());
		orderDTO.setTotalamount(order.getTotalamount());

		List<OrderItemDTO> orderItemDTOs = order.getOrderitems().stream().map(
				orderItem -> new OrderItemDTO(orderItem.getFooditem(), orderItem.getQuantity(), orderItem.getPrice()))
				.collect(Collectors.toList());

		orderDTO.setOrderitems(orderItemDTOs);

		return orderDTO;
	}

	public Order updateOrder(Long orderid, Order order) {
		Order existingOrder = orderRepository.findById(orderid)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderid));

		if (order.getStatus() != null) {
			existingOrder.setStatus(order.getStatus());
		}
		if (order.getTotalamount() != null) {
			existingOrder.setTotalamount(order.getTotalamount());
		}

		return orderRepository.save(existingOrder);
	}

	public void deleteOrder(Long orderid) {
		if (!orderRepository.existsById(orderid)) {
			throw new ResourceNotFoundException("order id not found:" + orderid);
		}
		orderRepository.deleteById(orderid);
	}

	public Order cancelOrder(Long orderid) {

		Order existingOrder = orderRepository.findById(orderid)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (!"CONFIRMED".equals(existingOrder.getStatus())) {
			throw new RuntimeException("Order cannot be cancelled. current status: " + existingOrder.getStatus());
		}

		existingOrder.setStatus("CANCELLED");
		return orderRepository.save(existingOrder);
	}

	public List<Order> getAllCancelOrder() {
		return orderRepository.findByStatus("CANCELLED");
	}

	public List<Order> getAllConfirmlOrder() {
		return orderRepository.findByStatus("CONFIRMED");
	}

}
