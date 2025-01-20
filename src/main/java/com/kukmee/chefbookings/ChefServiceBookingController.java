package com.kukmee.chefbookings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

@RestController
@RequestMapping("/api/chefbookings")
@CrossOrigin("*")
public class ChefServiceBookingController {

	@Autowired
	private ChefServiceBookingService bookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public ResponseEntity<?> createBooking(@RequestBody ChefServiceBooking booking) {
		try {
			bookingService.createBooking(booking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutBookingChef(booking.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
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
