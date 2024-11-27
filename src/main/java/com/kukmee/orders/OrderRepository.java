package com.kukmee.orders;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@EntityGraph(attributePaths = {"orderitems", "orderitems.fooditem"})
	List<Order> findByStatus(String status);
}
