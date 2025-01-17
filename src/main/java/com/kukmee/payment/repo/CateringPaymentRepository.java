package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.EventBookingPayment;

public interface CateringPaymentRepository extends JpaRepository<EventBookingPayment, Long>{

	EventBookingPayment findBySessionId( String sessionId);
}
