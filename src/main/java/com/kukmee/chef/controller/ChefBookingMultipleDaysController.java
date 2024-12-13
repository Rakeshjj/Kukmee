package com.kukmee.chef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.chef.ChefBookingMultipleDays;
import com.kukmee.chef.service.ChefbookingMultipleDaysService;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/multipledays")
public class ChefBookingMultipleDaysController {

	@Autowired
	private ChefbookingMultipleDaysService chefbookingMultipleDaysService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody ChefBookingMultipleDays chefBooking) {

		try {
			chefbookingMultipleDaysService.createBooking(chefBooking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutBookingCreationMultiple(chefBooking.getChefDayId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

}
