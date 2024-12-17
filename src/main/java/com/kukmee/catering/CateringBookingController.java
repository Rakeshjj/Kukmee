package com.kukmee.catering;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kukmee.payment.PaymentController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catering")
public class CateringBookingController {

	private final CateringBookingService cateringBookingService;

	@Autowired
	private PaymentController paymentController;

	public CateringBookingController(CateringBookingService cateringBookingService) {
		this.cateringBookingService = cateringBookingService;
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createCateringBooking(@Valid @RequestBody CateringBooking cateringBooking) {

		try {
			cateringBookingService.createCateringBooking(cateringBooking);
			ResponseEntity<?> paymentResponse = paymentController
					.checkoutCateringBooking(cateringBooking.getCateringId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<CateringBooking>> getAllBooking() {
		List<CateringBooking> cateringBookings = cateringBookingService.getAllBookings();
		return ResponseEntity.ok(cateringBookings);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/get")
	public ResponseEntity<?> getById(@RequestParam Long cateringId) {
		CateringBooking getByid = cateringBookingService.getById(cateringId);
		return ResponseEntity.ok(getByid);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteById(@RequestParam Long cateringId) {
		cateringBookingService.deleteCateringBooking(cateringId);
		return ResponseEntity.ok("Your booking deleted successfully");
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/update")
	public ResponseEntity<CateringBooking> update(@RequestBody CateringBooking cateringBooking,
			@RequestParam Long cateringId) {
		CateringBooking cateringBookingUpdate = cateringBookingService.updateCateringBooking(cateringBooking,
				cateringId);
		return ResponseEntity.ok(cateringBookingUpdate);

	}

}
