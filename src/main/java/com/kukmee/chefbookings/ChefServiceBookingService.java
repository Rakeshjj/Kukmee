package com.kukmee.chefbookings;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class ChefServiceBookingService {

	@Autowired
	private ChefServiceBookingRepository bookingRepository;

	public ChefServiceBooking createBooking(ChefServiceBooking booking) {

		if (booking.getServiceEndDate() != null && booking.getServiceStartDate().equals(booking.getServiceEndDate())) {
			throw new IllegalArgumentException("Start date and end date cannot be the same for multiple-day bookings.");
		}

		if (booking.getChefType() == null || booking.getChefType().isEmpty()) {
			throw new IllegalArgumentException("ChefType is required");
		}

		if (booking.getCuisine() == null || booking.getCuisine().isEmpty()) {
			throw new IllegalArgumentException("Categories is required");
		}

		if (booking.getServiceStartDate() == null) {
			throw new IllegalArgumentException("Service start date is required");
		}

		if (booking.getEventType() == null || booking.getEventType().isEmpty()) {
			throw new IllegalArgumentException("EventType is required");
		}

		if (booking.getLocation() == null || booking.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (booking.getMealPreferences() == null || booking.getMealPreferences().isEmpty()) {
			throw new IllegalArgumentException("Meal Preferences are required");
		}

		if (booking.getPhoneNumber() == null || String.valueOf(booking.getPhoneNumber()).length() != 10) {
			throw new IllegalArgumentException("Phone number must be exactly 10 digits");
		}

		if (booking.getFullName() == null || booking.getFullName().isEmpty()) {
			throw new IllegalArgumentException("Full Name is required");
		}

		double subtotal = booking.getGrandTotal();

		double gst = subtotal * 0.18;

		double totalAmount = gst + subtotal;

		booking.setGrandTotal(totalAmount);
		booking.setGst(gst);

		return bookingRepository.save(booking);
	}

	public List<ChefServiceBooking> getAllBookings() {
		return bookingRepository.findAll();
	}

	public ChefServiceBooking getBookingById(Long id) {
		return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
	}

	public ChefServiceBooking updateBooking(Long id, ChefServiceBooking updatedBooking) {
		if (bookingRepository.existsById(id)) {
			updatedBooking.setId(id);
			return bookingRepository.save(updatedBooking);
		}
		return null;
	}

	public void deleteBooking(Long id) {
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND :" + id);
		}
		bookingRepository.deleteById(id);
	}
}
