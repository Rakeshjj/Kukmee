package com.kukmee.chef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.chef.ChefMonthlyBooking;
import com.kukmee.chef.service.ChefMonthlyBookingService;

@RestController
@RequestMapping("/api/monthlybookings")
public class ChefMonthlyBookingController {

	@Autowired
	private ChefMonthlyBookingService chefBookingService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody ChefMonthlyBooking booking) {
		ChefMonthlyBooking createdBooking = chefBookingService.createBooking(booking);
		return ResponseEntity.ok(createdBooking);
	}

}
