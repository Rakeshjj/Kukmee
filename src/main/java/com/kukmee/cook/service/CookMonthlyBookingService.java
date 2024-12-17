package com.kukmee.cook.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.cook.CookBookingMonthly;
import com.kukmee.cook.repo.CookBookingRepoMonthly;
import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CookMonthlyBookingService {

	@Autowired
	private CookBookingRepoMonthly bookingRepository;

	public CookBookingMonthly createBooking(CookBookingMonthly cookBookingMonthly) {
		String newId = generateCustomId();
		cookBookingMonthly.setId(newId);

		double basePrice;

		switch (cookBookingMonthly.getNumberOfPeople()) {
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

		double gst = basePrice * 0.18;

		double totalAmount = basePrice + gst;

		cookBookingMonthly.setBasePrice(basePrice);
		cookBookingMonthly.setGst(gst);
		cookBookingMonthly.setTotalAmount(totalAmount);

		return bookingRepository.save(cookBookingMonthly);
	}

	public List<CookBookingMonthly> getAllBookings() {
		return bookingRepository.findAll();
	}

	public CookBookingMonthly getBookingById(String id) {
		return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id Not Found :" + id));
	}

	@Transactional
	public void deleteBooking(String id) {
		if (!bookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id Not Found :" + id);
		}
		bookingRepository.deleteById(id);
	}

	public CookBookingMonthly updateMonthlyBooking(CookBookingMonthly cookBookingMonthly, String id) {

		CookBookingMonthly updatedCookBooking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND: " + id));

		if (cookBookingMonthly.getNumberOfPeople() > 0) {
			updatedCookBooking.setNumberOfPeople(cookBookingMonthly.getNumberOfPeople());
		}

		if (Objects.nonNull(cookBookingMonthly.getStartDate()) && !"".equals(cookBookingMonthly.getStartDate())) {
			updatedCookBooking.setStartDate(cookBookingMonthly.getStartDate());
		}

		if (Objects.nonNull(cookBookingMonthly.getMealType()) && !"".equals(cookBookingMonthly.getMealType())) {
			updatedCookBooking.setMealType(cookBookingMonthly.getMealType());
		}

		if (Objects.nonNull(cookBookingMonthly.getHouseNumber()) && !"".equals(cookBookingMonthly.getHouseNumber())) {
			updatedCookBooking.setHouseNumber(cookBookingMonthly.getHouseNumber());
		}

		if (Objects.nonNull(cookBookingMonthly.getStreetArea()) && !"".equals(cookBookingMonthly.getStreetArea())) {
			updatedCookBooking.setStreetArea(cookBookingMonthly.getStreetArea());
		}

		if (Objects.nonNull(cookBookingMonthly.getCity()) && !"".equals(cookBookingMonthly.getCity())) {
			updatedCookBooking.setCity(cookBookingMonthly.getCity());
		}

		if (cookBookingMonthly.getBasePrice() >= 0) {
			updatedCookBooking.setBasePrice(cookBookingMonthly.getBasePrice());
		}

		if (cookBookingMonthly.getGst() >= 0) {
			updatedCookBooking.setGst(cookBookingMonthly.getGst());
		}

		if (cookBookingMonthly.getTotalAmount() >= 0) {
			updatedCookBooking.setTotalAmount(cookBookingMonthly.getTotalAmount());
		}

		return bookingRepository.save(updatedCookBooking);
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
