package com.kukmee.cateringbooking;

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
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.payment.PaymentController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/event")
public class EventBookingController {

	@Autowired
	private EventBookingService eventBookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public ResponseEntity<?> createCateringBooking(@Valid @RequestBody EventBooking booking) {

		try {
			eventBookingService.createBooking(booking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutCateringBooking(booking.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<EventBooking>> getAllBookings() {
		List<EventBooking> getAllBooking = eventBookingService.getAllBookings();
		return ResponseEntity.ok(getAllBooking);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventBooking> getBookingById(@PathVariable Long id) {
		EventBooking eventBooking = eventBookingService.getBookingById(id);

		return ResponseEntity.ok(eventBooking);
	}

	@PutMapping("/{id}")
	public EventBooking updateBooking(@PathVariable Long id, @RequestBody EventBooking updatedBooking) {
		return eventBookingService.updateBooking(id, updatedBooking);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
		eventBookingService.deleteBooking(id);

		return ResponseEntity.ok("Deleted successfully!!!");
	}
}
