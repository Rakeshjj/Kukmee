package com.kukmee.bookingcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.bookingentity.BartenderBooking;
import com.kukmee.bookingservice.BartenderBookingService;

@RestController
@RequestMapping("/api/bartender")
@CrossOrigin("*")
public class BartenderBookingController {

	@Autowired
	private BartenderBookingService bartenderBookingService;

	// Create a new bartender booking
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/book")
	public ResponseEntity<String> bookBartender(@RequestBody BartenderBooking booking) {
		BartenderBooking savedBooking = bartenderBookingService.createBooking(booking);
		return ResponseEntity.ok("Booking Successful, Booking ID: " + savedBooking.getBookingid());
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/update-payment/{bookingid}")
	public ResponseEntity<String> updatePayment(@PathVariable Long bookingid, @RequestParam Double remainingPayment) {
		try {
			bartenderBookingService.updatePaymentDetails(bookingid, remainingPayment);
			return ResponseEntity.ok("Payment Updated Successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update payment: " + e.getMessage());
		}
	}

	// Get all bookings (admin-only access)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/allBookings")
	public ResponseEntity<List<BartenderBooking>> getAllBartenderBookings() {
		return ResponseEntity.ok(bartenderBookingService.getAllBartenderBookings());
	}
}
