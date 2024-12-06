package com.kukmee.catering;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catering")
public class CateringBookingController {

	private final CateringBookingService cateringBookingService;

	public CateringBookingController(CateringBookingService cateringBookingService) {
		this.cateringBookingService = cateringBookingService;
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<CateringBooking> createCateringBooking(@Valid @RequestBody CateringBooking cateringBooking) {
		CateringBooking createdBooking = cateringBookingService.createCateringBooking(cateringBooking);
		return ResponseEntity.ok(createdBooking);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<CateringBooking>> getAllBooking() {
		List<CateringBooking> cateringBookings = cateringBookingService.getAllBookings();
		return ResponseEntity.ok(cateringBookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<?> getById(@RequestParam Long id) {
		CateringBooking getByid = cateringBookingService.getById(id);
		return ResponseEntity.ok(getByid);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteById(@RequestParam Long id) {
		cateringBookingService.deleteCateringBooking(id);
		return ResponseEntity.ok("Your booking deleted successfully");
	}

}
