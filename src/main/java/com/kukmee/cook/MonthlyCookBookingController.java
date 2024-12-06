package com.kukmee.cook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monthlycook")
public class MonthlyCookBookingController {

	@Autowired
	private MonthlyCookBookingService bookingService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody MonthlyCookBooking booking,
			@RequestParam(defaultValue = "0.0") double couponDiscount) {

		MonthlyCookBooking savedBooking = bookingService.createBooking(booking, couponDiscount);
		return ResponseEntity.ok(savedBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<MonthlyCookBooking>> getAllBookings() {
		List<MonthlyCookBooking> monthlyCookBookings = bookingService.getAllBookings();
		return ResponseEntity.ok(monthlyCookBookings);
	}

	@GetMapping("/get")
	public ResponseEntity<?> getById(Long id) {
		MonthlyCookBooking monthlyCookBooking = bookingService.getById(id);
		return ResponseEntity.ok(monthlyCookBooking);
	}

	public ResponseEntity<?> deleteById(Long id) {
		bookingService.deleteById(id);
		return ResponseEntity.ok("Deleted successfully");
	}
}
