package com.kukmee.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
	public Plan findByPlanName(String planName);
}
