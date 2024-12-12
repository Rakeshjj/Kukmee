package com.kukmee.chef.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.chef.ChefBookingMultipleDays;

public interface ChefBookingMultipleDaysRepo extends JpaRepository<ChefBookingMultipleDays, String>{
    
	@Query("SELECT chefDayId FROM ChefBookingMultipleDays ORDER BY chefDayId DESC LIMIT 1")
	String findLastBookingId();
}
