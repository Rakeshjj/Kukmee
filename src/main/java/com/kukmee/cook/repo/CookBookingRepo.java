package com.kukmee.cook.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.cook.CookBooking;

public interface CookBookingRepo extends JpaRepository<CookBooking, String>{

	@Query("SELECT cookBookingId FROM CookBooking ORDER BY cookBookingId DESC LIMIT 1")
	String findLastBookingId();
}
