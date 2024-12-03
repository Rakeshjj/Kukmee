package com.kukmee.cook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domestic-cook-bookings")
public class CookBookingController {

	@Autowired
	private DomesticCookBookingService domesticCookBookingService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createDomesticCookBooking(@RequestBody DomesticCookBooking domesticCookBooking) {
		DomesticCookBooking booking = domesticCookBookingService.createDomesticCookBooking(domesticCookBooking);
		return ResponseEntity.ok(booking);
	}
}
