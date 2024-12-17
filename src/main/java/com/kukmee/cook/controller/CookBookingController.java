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

import com.kukmee.cook.CookBookingOneMeal;
import com.kukmee.cook.service.CookBookingServiceOneMeal;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/cook")
public class CookBookingController {

	@Autowired
	private CookBookingServiceOneMeal cookBookingServiceOneMeal;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody CookBookingOneMeal cookBookingOneMeal) {

		try {
			cookBookingServiceOneMeal.createBooking(cookBookingOneMeal);
			ResponseEntity<?> paymentResponse = paymentController
					.checkoutBookingCreation(cookBookingOneMeal.getCookBookingId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/lunch")
	public ResponseEntity<?> createLunchBooking(@RequestBody CookBookingOneMeal cookBookingOneMeal) {
		cookBookingServiceOneMeal.createBooking(cookBookingOneMeal);
		return ResponseEntity.ok("Lunch booking created successfully!");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/dinner")
	public ResponseEntity<?> createDinnerBooking(@RequestBody CookBookingOneMeal cookBookingOneMeal) {
		cookBookingServiceOneMeal.createBooking(cookBookingOneMeal);
		return ResponseEntity.ok("Dinner booking created successfully!");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<CookBookingOneMeal> getBooking(@RequestParam String cookBookingId) {
		CookBookingOneMeal chefBooking = cookBookingServiceOneMeal.getBookingById(cookBookingId);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<CookBookingOneMeal>> getAllbookings() {
		List<CookBookingOneMeal> bookings = cookBookingServiceOneMeal.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@RequestParam String cookBookingId) {
		cookBookingServiceOneMeal.deleteBooking(cookBookingId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody CookBookingOneMeal cookBookingOneMeal,
			@RequestParam String cookBookingId) {
		cookBookingServiceOneMeal.updateCookBooking(cookBookingOneMeal, cookBookingId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

}