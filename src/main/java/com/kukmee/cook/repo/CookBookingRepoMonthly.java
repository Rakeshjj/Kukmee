package com.kukmee.cook.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.cook.CookBookingMonthly;

public interface CookBookingRepoMonthly extends JpaRepository<CookBookingMonthly, String> {

	@Query("SELECT id FROM CookBookingMonthly ORDER BY id DESC LIMIT 1")
	String findLastBookingId();
}
