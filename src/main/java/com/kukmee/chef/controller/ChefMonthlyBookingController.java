package com.kukmee.chef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.chef.ChefMonthlyBooking;
import com.kukmee.chef.service.ChefMonthlyBookingService;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/monthlybookings")
public class ChefMonthlyBookingController {

	@Autowired
	private ChefMonthlyBookingService chefBookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody ChefMonthlyBooking chefBooking) {

		try {
			chefBookingService.createBooking(chefBooking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutBookingCreationMonthly(chefBooking.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

}
