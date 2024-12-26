package com.kukmee.foodorders;

import java.util.List;

public interface OrderService {

	public Order saveOrder(Long customerid,Order order);

	public Order getOrderById(Long orderId);

	public List<Order> getAllOrders();

	public void deleteOrder(Long orderId);
}
