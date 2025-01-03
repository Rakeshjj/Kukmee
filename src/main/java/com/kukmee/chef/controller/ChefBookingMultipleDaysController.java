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
			ResponseEntity<?> paymentResponse = paymentController
					.checkoutBookingCreationMultiple(chefBooking.getChefDayId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<ChefBookingMultipleDays> getBooking(@RequestParam String chefDayId) {
		ChefBookingMultipleDays chefBooking = chefbookingMultipleDaysService.getById(chefDayId);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<ChefBookingMultipleDays>> getAllbookings() {
		List<ChefBookingMultipleDays> bookings = chefbookingMultipleDaysService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteBooking(@RequestParam String chefDayId) {
		chefbookingMultipleDaysService.deleteById(chefDayId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody ChefBookingMultipleDays chefBooking, @RequestParam String chefDayId) {
		chefbookingMultipleDaysService.updateChefBooking(chefBooking, chefDayId);
		return ResponseEntity.ok("Booking Updated successfully.");
	}

}
