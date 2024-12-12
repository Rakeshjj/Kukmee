package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.CookBookingPayment;

public interface CookBookingPaymentRepo extends JpaRepository<CookBookingPayment, Long>{
  
	CookBookingPayment findBySessionId( String sessionId);

}
