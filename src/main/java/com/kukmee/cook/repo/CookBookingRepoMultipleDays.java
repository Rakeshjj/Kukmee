package com.kukmee.cook.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.cook.CookBookingMultipleDays;

public interface CookBookingRepoMultipleDays extends JpaRepository<CookBookingMultipleDays, String> {
  
	@Query("SELECT cookDayId FROM CookBookingMultipleDays ORDER BY cookDayId DESC LIMIT 1")
	String findLastBookingId();
}
