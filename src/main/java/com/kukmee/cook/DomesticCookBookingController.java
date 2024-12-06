package com.kukmee.cook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domesticcookbookings")
public class DomesticCookBookingController {

	@Autowired
	private DomesticCookBookingService domesticCookBookingService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createDomesticCookBooking(@RequestBody DomesticCookBooking domesticCookBooking) {
		DomesticCookBooking booking = domesticCookBookingService.createDomesticCookBooking(domesticCookBooking);
		return ResponseEntity.ok(booking);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<DomesticCookBooking>> getAll() {
		List<DomesticCookBooking> getAllBookings = domesticCookBookingService.getAll();
		return ResponseEntity.ok(getAllBookings);
	}

	@GetMapping("/get")
	public ResponseEntity<?> getById(@RequestParam Long id) {
		DomesticCookBooking domesticCookBooking = domesticCookBookingService.getById(id);
		return ResponseEntity.ok(domesticCookBooking);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteById(Long id) {
		domesticCookBookingService.deleteById(id);
		return ResponseEntity.ok("Deleted successfully...");
	}
}
