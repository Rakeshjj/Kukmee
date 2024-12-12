package com.kukmee.cook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kukmee.payment.PaymentController;

import java.util.List;

@RestController
@RequestMapping("/api/monthlycook")
public class MonthlyCookBookingController {

	@Autowired
	private MonthlyCookBookingService bookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody MonthlyCookBooking booking,
			@RequestParam(defaultValue = "0.0") double couponDiscount) {

		try {
			bookingService.createBooking(booking, couponDiscount);
			ResponseEntity<?> paymentResponse = paymentController.checkoutCook(booking.getMonthlyCookId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}

	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/getAll")
	public ResponseEntity<List<MonthlyCookBooking>> getAllBookings() {
		List<MonthlyCookBooking> monthlyCookBookings = bookingService.getAllBookings();
		return ResponseEntity.ok(monthlyCookBookings);
	}

	@GetMapping("/get")
	public ResponseEntity<?> getById(Long monthlyCookId) {
		MonthlyCookBooking monthlyCookBooking = bookingService.getById(monthlyCookId);
		return ResponseEntity.ok(monthlyCookBooking);
	}

	public ResponseEntity<?> deleteById(Long monthlyCookId) {
		bookingService.deleteById(monthlyCookId);
		return ResponseEntity.ok("Deleted successfully");
	}
}
