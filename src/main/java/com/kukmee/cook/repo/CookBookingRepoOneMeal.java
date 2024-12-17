package com.kukmee.cook.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kukmee.cook.CookBookingOneMeal;

public interface CookBookingRepoOneMeal extends JpaRepository<CookBookingOneMeal, String>{

	@Query("SELECT cookBookingId FROM CookBookingOneMeal ORDER BY cookBookingId DESC LIMIT 1")
	String findLastBookingId();
}
