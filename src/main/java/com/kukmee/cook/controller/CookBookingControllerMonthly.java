package com.kukmee.cook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.cook.CookBookingMonthly;
import com.kukmee.cook.service.CookMonthlyBookingService;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/monthlybookings")
public class CookBookingControllerMonthly {

	@Autowired
	private CookMonthlyBookingService cookBookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/cook")
	public ResponseEntity<?> createBooking(@RequestBody CookBookingMonthly cookBookingMonthly) {

		try {
			cookBookingService.createBooking(cookBookingMonthly);
			ResponseEntity<?> paymentResponse = paymentController.checkoutBookingCreationMonthly(cookBookingMonthly.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<CookBookingMonthly> getBooking(@RequestParam String id) {
		CookBookingMonthly chefBooking = cookBookingService.getBookingById(id);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<CookBookingMonthly>> getAllbookings() {
		List<CookBookingMonthly> bookings = cookBookingService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@RequestParam String id) {
		cookBookingService.deleteBooking(id);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody CookBookingMonthly cookBookingMonthly,
			@RequestParam String id) {
		cookBookingService.updateMonthlyBooking(cookBookingMonthly, id);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

}
