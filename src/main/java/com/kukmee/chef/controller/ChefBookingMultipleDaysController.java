package com.kukmee.chef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.chef.ChefBookingMultipleDays;
import com.kukmee.chef.service.ChefbookingMultipleDaysService;

@RestController
@RequestMapping("/api/multipledays")
public class ChefBookingMultipleDaysController {

	@Autowired
	private ChefbookingMultipleDaysService chefbookingMultipleDaysService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBookingMultipleDays(@RequestBody ChefBookingMultipleDays chefBookingMultipleDays) {
		chefbookingMultipleDaysService.createBooking(chefBookingMultipleDays);
		return ResponseEntity.ok("Booking created succeffully");

	}
}
