package com.kukmee.chef.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.chef.ChefMonthlyBooking;
import com.kukmee.chef.repo.ChefMonthlyBookingRepository;

@Service
public class ChefMonthlyBookingService {

	@Autowired
	private ChefMonthlyBookingRepository bookingRepository;

	public ChefMonthlyBooking createBooking(ChefMonthlyBooking booking) {
		String newId = generateCustomId();
		booking.setId(newId);

		double basePrice;

		switch (booking.getNumberOfPeople()) {
		case 1:
			basePrice = 3069;
			break;
		case 2:
			basePrice = 3169;
			break;
		case 3:
			basePrice = 3319;
			break;
		case 4:
			basePrice = 3569;
			break;
		case 5:
			basePrice = 3819;
			break;
		case 6:
			basePrice = 4069;
			break;
		default:
			throw new IllegalArgumentException("Invalid number of people. Must be between 1 and 6.");
		}

		// Calculate GST (18%)
		double gst = basePrice * 0.18;

		// Calculate Total Amount
		double totalAmount = basePrice + gst;

		booking.setBasePrice(basePrice);
		booking.setGst(gst);
		booking.setTotalAmount(totalAmount);

		return bookingRepository.save(booking);
	}

	public List<ChefMonthlyBooking> getAllBookings() {
		return bookingRepository.findAll();
	}
	
	public Optional<ChefMonthlyBooking> getById(String id){
		return bookingRepository.findById(id);
	}

	private String generateCustomId() {
		String lastId = bookingRepository.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("ch")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "ch" + nextNumber;
	}
}
