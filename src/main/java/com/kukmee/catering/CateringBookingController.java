package com.kukmee.catering;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catering")
public class CateringBookingController {

	private final CateringBookingService cateringBookingService;

	public CateringBookingController(CateringBookingService cateringBookingService) {
		this.cateringBookingService = cateringBookingService;
	}

	@PostMapping
	public ResponseEntity<CateringBooking> createCateringBooking(@Valid @RequestBody CateringBooking cateringBooking) {
		CateringBooking createdBooking = cateringBookingService.createCateringBooking(cateringBooking);
		return ResponseEntity.ok(createdBooking);
	}
}
