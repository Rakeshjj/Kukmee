package com.kukmee.chef.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.chef.ChefMonthlyBooking;
import com.kukmee.chef.repo.ChefMonthlyBookingRepository;
import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

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

	public ChefMonthlyBooking getBookingById(String id) {
		return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id Not Found :" + id));
	}

	@Transactional
	public void deleteBooking(String id) {
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id Not Found :" + id);
		}
		bookingRepository.deleteById(id);
	}

	public ChefMonthlyBooking updateMonthlyBooking(ChefMonthlyBooking chefMonthlyBooking, String id) {
		ChefMonthlyBooking updateMonthlyBooking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND: " + id));

		if (chefMonthlyBooking.getNumberOfPeople() > 0) {
			updateMonthlyBooking.setNumberOfPeople(chefMonthlyBooking.getNumberOfPeople());
		}

		if (Objects.nonNull(chefMonthlyBooking.getStartDate()) && !"".equals(chefMonthlyBooking.getStartDate())) {
			updateMonthlyBooking.setStartDate(chefMonthlyBooking.getStartDate());
		}

		if (Objects.nonNull(chefMonthlyBooking.getMealType()) && !"".equals(chefMonthlyBooking.getMealType())) {
			updateMonthlyBooking.setMealType(chefMonthlyBooking.getMealType());
		}

		if (Objects.nonNull(chefMonthlyBooking.getHouseNumber()) && !"".equals(chefMonthlyBooking.getHouseNumber())) {
			updateMonthlyBooking.setHouseNumber(chefMonthlyBooking.getHouseNumber());
		}

		if (Objects.nonNull(chefMonthlyBooking.getStreetArea()) && !"".equals(chefMonthlyBooking.getStreetArea())) {
			updateMonthlyBooking.setStreetArea(chefMonthlyBooking.getStreetArea());
		}

		if (Objects.nonNull(chefMonthlyBooking.getCity()) && !"".equals(chefMonthlyBooking.getCity())) {
			updateMonthlyBooking.setCity(chefMonthlyBooking.getCity());
		}

		if (chefMonthlyBooking.getBasePrice() >= 0) {
			updateMonthlyBooking.setBasePrice(chefMonthlyBooking.getBasePrice());
		}

		if (chefMonthlyBooking.getGst() >= 0) {
			updateMonthlyBooking.setGst(chefMonthlyBooking.getGst());
		}

		if (chefMonthlyBooking.getTotalAmount() >= 0) {
			updateMonthlyBooking.setTotalAmount(chefMonthlyBooking.getTotalAmount());
		}

		return bookingRepository.save(updateMonthlyBooking);
	}

	private String generateCustomId() {
		String lastId = bookingRepository.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("mb")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "mb" + nextNumber;
	}
}
