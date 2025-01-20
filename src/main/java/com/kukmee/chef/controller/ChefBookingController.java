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

import com.kukmee.chef.ChefBooking;
import com.kukmee.chef.service.ChefBookingService;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/bookings")
public class ChefBookingController {

	private final ChefBookingService chefBookingService;

	@Autowired
	private PaymentController paymentController;

	public ChefBookingController(ChefBookingService chefBookingService) {
		super();
		this.chefBookingService = chefBookingService;
	}

//	@PreAuthorize("hasRole('CUSTOMER')")
//	@PostMapping
//	public ResponseEntity<?> createBooking(@RequestBody ChefBooking chefBooking) {
//
//		try {
//			chefBookingService.createBooking(chefBooking);
//			ResponseEntity<?> paymentResponse = paymentController
//					.checkoutBookingCreation(chefBooking.getChefBookingId());
//
//			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Order creation failed: " + e.getMessage());
//		}
//	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/lunch")
	public ResponseEntity<?> createLunchBooking(@RequestBody ChefBooking chefBooking) {
		chefBookingService.createBooking(chefBooking);
		return ResponseEntity.ok("Lunch booking created successfully!");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/dinner")
	public ResponseEntity<?> createDinnerBooking(@RequestBody ChefBooking chefBooking) {
		chefBookingService.createBooking(chefBooking);
		return ResponseEntity.ok("Dinner booking created successfully!");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<ChefBooking> getBooking(@RequestParam String chefBookingId) {
		ChefBooking chefBooking = chefBookingService.getBookingById(chefBookingId);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<ChefBooking>> getAllbookings() {
		List<ChefBooking> bookings = chefBookingService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteBooking(@RequestParam String chefBookingId) {
		chefBookingService.deleteBooking(chefBookingId);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody ChefBooking chefBooking, @RequestParam String chefBookingId) {
		chefBookingService.updateChefBooking(chefBooking, chefBookingId);
		return ResponseEntity.ok("Booking Updated successfully.");
	}

}
