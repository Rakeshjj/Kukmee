package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.CateringBookingPayment;

public interface CateringPaymentRepository extends JpaRepository<CateringBookingPayment, Long>{

	CateringBookingPayment findBySessionId( String sessionId);
}
