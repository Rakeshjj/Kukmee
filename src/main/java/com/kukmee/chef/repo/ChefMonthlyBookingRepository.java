package com.kukmee.chef.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.chef.ChefMonthlyBooking;

public interface ChefMonthlyBookingRepository extends JpaRepository<ChefMonthlyBooking, String> {

	@Query("SELECT id FROM ChefMonthlyBooking ORDER BY id DESC LIMIT 1")
	String findLastBookingId();
}
