package com.kukmee.chefbookings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chefbookings")
public class ChefServiceBookingController {

	@Autowired
	private ChefServiceBookingService bookingService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public ResponseEntity<?> createBooking(@RequestBody ChefServiceBooking booking) {
		bookingService.createBooking(booking);
		return ResponseEntity.ok("chef created succesfully");
	}

	@GetMapping
	public ResponseEntity<List<ChefServiceBooking>> getAllBookings() {
		List<ChefServiceBooking> chefServiceBookings = bookingService.getAllBookings();
		return ResponseEntity.ok(chefServiceBookings);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBookingById(@PathVariable Long id) {
		ChefServiceBooking chefServiceBooking = bookingService.getBookingById(id);
		return ResponseEntity.ok(chefServiceBooking);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody ChefServiceBooking updatedBooking) {
		bookingService.updateBooking(id, updatedBooking);
		return ResponseEntity.ok("Updated Successfully");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
		bookingService.deleteBooking(id);
		return ResponseEntity.ok("Deleted Succcessfully");
	}
}
