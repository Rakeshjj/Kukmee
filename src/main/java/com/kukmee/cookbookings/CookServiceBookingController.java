package com.kukmee.cookbookings;

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
@RequestMapping("/api/cookbookings")
@CrossOrigin("*")
public class CookServiceBookingController {

	@Autowired
	private CookBookingService bookingService;

	@Autowired
	private PaymentController paymentController;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/create")
	public ResponseEntity<?> createBooking(@RequestBody CookServiceBooking booking) {
		try {
			bookingService.createBooking(booking);
			ResponseEntity<?> paymentResponse = paymentController.checkoutBookingCook(booking.getId());

			return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse.getBody());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order creation failed: " + e.getMessage());
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<CookServiceBooking>> getAllBookings() {
		List<CookServiceBooking> cookServiceBookings = bookingService.getAllBookings();
		return ResponseEntity.ok(cookServiceBookings);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBookingById(@PathVariable Long id) {
		CookServiceBooking bookingService1 = bookingService.getBookingById(id);
		return ResponseEntity.ok(bookingService1);
	}

	@PutMapping("/{id}")
	public CookServiceBooking updateBooking(@PathVariable Long id, @RequestBody CookServiceBooking updatedBooking) {
		return bookingService.updateBooking(id, updatedBooking);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
		bookingService.deleteBooking(id);
		return ResponseEntity.ok("Deleted successfully!!!");
	}
}
