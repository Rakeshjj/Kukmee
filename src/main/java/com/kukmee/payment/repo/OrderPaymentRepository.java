package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.OrderPayment;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {

	OrderPayment findBySessionId(String sessionId);
}
