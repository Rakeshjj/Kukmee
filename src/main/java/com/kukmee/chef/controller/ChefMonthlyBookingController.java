package com.kukmee.chef.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<ChefMonthlyBooking> getBooking(@RequestParam String id) {
		ChefMonthlyBooking chefBooking = chefBookingService.getBookingById(id);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<ChefMonthlyBooking>> getAllbookings() {
		List<ChefMonthlyBooking> bookings = chefBookingService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteBooking(@RequestParam String id) {
		chefBookingService.deleteBooking(id);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody ChefMonthlyBooking chefBooking, @RequestParam String id) {
		chefBookingService.updateMonthlyBooking(chefBooking, id);
		return ResponseEntity.ok("Booking Updated successfully.");
	}

}
