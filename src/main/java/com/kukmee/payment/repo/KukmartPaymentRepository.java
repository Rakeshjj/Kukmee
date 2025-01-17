package com.kukmee.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.payment.KukmartPayment;

public interface KukmartPaymentRepository extends JpaRepository<KukmartPayment, Long> {

	public KukmartPayment findBySessionId(String sessionId);

}
