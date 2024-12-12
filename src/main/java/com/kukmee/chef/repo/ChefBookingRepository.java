package com.kukmee.chef.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.chef.ChefBooking;

public interface ChefBookingRepository extends JpaRepository<ChefBooking, Long> {
}
