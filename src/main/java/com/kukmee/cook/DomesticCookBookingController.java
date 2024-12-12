package com.kukmee.cook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kukmee.orders.Order;
import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/domesticcookbookings")
public class DomesticCookBookingController {

	@Autowired
	private DomesticCookBookingService domesticCookBookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> createDomesticCookBooking(@RequestBody DomesticCookBooking domesticCookBooking) {

		try {
			domesticCookBookingService.createDomesticCookBooking(domesticCookBooking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutCook(domesticCookBooking.getCookId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<DomesticCookBooking>> getAll() {
		List<DomesticCookBooking> getAllBookings = domesticCookBookingService.getAll();
		return ResponseEntity.ok(getAllBookings);
	}

	@GetMapping("/get")
	public ResponseEntity<?> getById(@RequestParam Long cookId) {
		DomesticCookBooking domesticCookBooking = domesticCookBookingService.getById(cookId);
		return ResponseEntity.ok(domesticCookBooking);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteById(Long cookId) {
		domesticCookBookingService.deleteById(cookId);
		return ResponseEntity.ok("Deleted successfully...");
	}
}
