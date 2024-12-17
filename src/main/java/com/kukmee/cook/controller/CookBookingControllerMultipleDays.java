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

import com.kukmee.cook.CookBookingMultipleDays;
import com.kukmee.cook.service.CookbookingMultipleDaysService;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/bookings")
public class CookBookingControllerMultipleDays {

	@Autowired
	private CookbookingMultipleDaysService cookbookingMultipleDaysService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/cookmultiple")
	public ResponseEntity<?> createBooking(@RequestBody CookBookingMultipleDays cookBooking) {

		try {
			cookbookingMultipleDaysService.createBooking(cookBooking);
			ResponseEntity<?> paymentResponse = paymentController
					.checkoutBookingCreationMultiple(cookBooking.getCookDayId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<CookBookingMultipleDays> getBooking(@RequestParam String cookDayId) {
		CookBookingMultipleDays chefBooking = cookbookingMultipleDaysService.getBookingById(cookDayId);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<CookBookingMultipleDays>> getAllbookings() {
		List<CookBookingMultipleDays> bookings = cookbookingMultipleDaysService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@RequestParam String cookDayId) {
		cookbookingMultipleDaysService.deleteBooking(cookDayId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody CookBookingMultipleDays cookBookingMultipleDays,
			@RequestParam String cookDayId) {
		cookbookingMultipleDaysService.updateCookBooking(cookBookingMultipleDays, cookDayId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

}
