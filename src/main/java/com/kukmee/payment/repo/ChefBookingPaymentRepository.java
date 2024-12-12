package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.ChefBookingPayment;

public interface ChefBookingPaymentRepository extends JpaRepository<ChefBookingPayment, Long> {

	ChefBookingPayment findBySessionId(String sessionId);
}
