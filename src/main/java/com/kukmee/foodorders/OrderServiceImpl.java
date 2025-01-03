package com.kukmee.foodorders;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private static final BigDecimal GST_RATE = new BigDecimal("0.18");

	@Override
	public Order saveOrder(Long customerid, Order order) {
		Customer customer = customerRepository.findById(customerid)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

		order.setCustomer(customer);
		order.setStatus("Pending");

		BigDecimal totalAmountWithoutGST = BigDecimal.ZERO;

		for (FoodItem item : order.getFoodItems()) {
			item.setOrder(order); 
			BigDecimal itemTotal = item.getFoodprice().multiply(BigDecimal.valueOf(item.getQuantity()));
			totalAmountWithoutGST = totalAmountWithoutGST.add(itemTotal);
		}

		BigDecimal gstAmount = totalAmountWithoutGST.multiply(GST_RATE);
		BigDecimal totalAmountWithGST = totalAmountWithoutGST.add(gstAmount);

		order.setTotalamount(totalAmountWithGST);
		order.setGstAmount(gstAmount);

		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) {
		if (!orderRepository.existsById(orderId)) {
			throw new RuntimeException("Order not found with ID: " + orderId);
		}
		orderRepository.deleteById(orderId);
	}
}
