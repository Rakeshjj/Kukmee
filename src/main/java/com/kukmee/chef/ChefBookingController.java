package com.kukmee.chef;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class ChefBookingController {

	private final ChefBookingService chefBookingService;

	public ChefBookingController(ChefBookingService chefBookingService) {
		super();
		this.chefBookingService = chefBookingService;
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody ChefBooking chefBooking) {
		chefBookingService.createBooking(chefBooking);
		return ResponseEntity.ok("Booking creation successfully...");
	}

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
	public ResponseEntity<ChefBooking> getBooking(@RequestParam Long id) {
		ChefBooking chefBooking = chefBookingService.getBookingById(id);
		return ResponseEntity.ok(chefBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<ChefBooking>> getAllbookings() {
		List<ChefBooking> bookings = chefBookingService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@RequestParam Long id) {
		chefBookingService.deleteBooking(id);
		return ResponseEntity.ok("Booking deleted successfully.");
	}

}
