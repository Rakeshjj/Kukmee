package com.kukmee.cookbookings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class CookBookingService {

	@Autowired
	private CookServiceBookingRepository cookServiceBookingRepository;

	public CookServiceBooking createBooking(CookServiceBooking booking) {

		if (booking.getServiceStartDate() == null || booking.getServiceStartDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (booking.getLocation() == null || booking.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (booking.getPhoneNumber() == null || String.valueOf(booking.getPhoneNumber()).length() != 10) {
			throw new IllegalArgumentException("Phone number is required & exactly 10 digits");
		}
		return cookServiceBookingRepository.save(booking);
	}

	public List<CookServiceBooking> getAllBookings() {
		return cookServiceBookingRepository.findAll();
	}

	public CookServiceBooking getBookingById(Long id) {
		return cookServiceBookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
	}

	public CookServiceBooking updateBooking(Long id, CookServiceBooking cookServiceBooking) {
		if (cookServiceBookingRepository.existsById(id)) {
			cookServiceBooking.setId(id);

			return cookServiceBookingRepository.save(cookServiceBooking);
		}
		return null;
	}

	public void deleteBooking(Long id) {
		if (!cookServiceBookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND :" + id);
		}
		cookServiceBookingRepository.deleteById(id);
	}
}
