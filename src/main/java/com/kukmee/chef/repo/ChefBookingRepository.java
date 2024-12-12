package com.kukmee.chef.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.chef.ChefBooking;

public interface ChefBookingRepository extends JpaRepository<ChefBooking, String> {
	
	@Query("SELECT chefBookingId FROM ChefBooking ORDER BY chefBookingId DESC LIMIT 1")
	String findLastBookingId();
}
