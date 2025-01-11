package com.kukmee.cateringbooking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class EventBookingService {

	@Autowired
	private EventBookingRepository eventBookingRepository;

	public EventBooking createBooking(EventBooking booking) {
		
		if (booking.getEventDate() == null || booking.getEventDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (booking.getEventLocation() == null || booking.getEventLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}
		
		if (booking.getEventType() == null || booking.getEventType().isEmpty()) {
			throw new IllegalArgumentException("Event type is required");
		}

		if (booking.getPhoneNumber() == null || String.valueOf(booking.getPhoneNumber()).length() != 10) {
			throw new IllegalArgumentException("Phone number is required & exactly 10 digits");
		}
		
		return eventBookingRepository.save(booking);
	}

	public List<EventBooking> getAllBookings() {
		return eventBookingRepository.findAll();
	}

	public EventBooking getBookingById(Long id) {
		return eventBookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
	}

	public EventBooking updateBooking(Long id, EventBooking updatedBooking) {
		if (eventBookingRepository.existsById(id)) {
			updatedBooking.setId(id);
			return eventBookingRepository.save(updatedBooking);
		}
		return null;
	}

	public void deleteBooking(Long id) {
		if (eventBookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND :" + id);
		}
		eventBookingRepository.deleteById(id);
	}
}
