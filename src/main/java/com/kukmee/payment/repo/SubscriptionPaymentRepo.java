package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.SubscriptionPayment;

public interface SubscriptionPaymentRepo extends JpaRepository<SubscriptionPayment, Long> {

	SubscriptionPayment findBySessionId(String sessionId);
}
