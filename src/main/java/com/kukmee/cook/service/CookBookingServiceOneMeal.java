package com.kukmee.cook.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.cook.CookBooking;
import com.kukmee.cook.repo.CookBookingRepo;
import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CookBookingServiceOneMeal {

	@Autowired
	private CookBookingRepo cookBookingRepo;

	public CookBooking createBooking(CookBooking cookBookingOneMeal) {

		String newId = generateCustomId();
		cookBookingOneMeal.setCookBookingId(newId);

		if (cookBookingOneMeal.getOccasion() == null || cookBookingOneMeal.getOccasion().isEmpty()) {
			throw new IllegalArgumentException("Occasion is required");
		}

		if (cookBookingOneMeal.getDate() == null || cookBookingOneMeal.getDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (cookBookingOneMeal.getLocation() == null || cookBookingOneMeal.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (cookBookingOneMeal.getMealType() == null || cookBookingOneMeal.getMealType().isEmpty()) {
			throw new IllegalArgumentException("Meal type (Breakfast, Lunch, Dinner) is required.");
		}

		if ((cookBookingOneMeal.getMealType().equalsIgnoreCase("lunch")
				|| cookBookingOneMeal.getMealType().equalsIgnoreCase("dinner"))
				&& (cookBookingOneMeal.getCuisine() == null || cookBookingOneMeal.getCuisine().isEmpty())) {
			throw new IllegalArgumentException("Cuisine is required for lunch and dinner bookings.");
		}

		String subscriptionDuration = cookBookingOneMeal.getSubscriptionDuration();
		LocalDate startDate = LocalDate.parse(cookBookingOneMeal.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalDate endDate = calculateEndDate(startDate, subscriptionDuration);

		cookBookingOneMeal.setEndDate(endDate.toString());

		if (cookBookingOneMeal.getMealType().equalsIgnoreCase("breakfast")) {
			cookBookingOneMeal.setCuisine(null);
		}

		int people = cookBookingOneMeal.getNumberOfPeople();
		
		double basePrice = 219 + ((people - 1) / 4) * 200;
		double dishCharges = cookBookingOneMeal.getDishes().size() * 200;
		double discount = 0;
		if (cookBookingOneMeal.getDishes().size() > 5) {
			discount = 100;
		}

		double subtotal = basePrice + dishCharges - discount;

		double gst = subtotal * 0.18;

		double totalAmount = subtotal + gst;
		double advance = totalAmount * 0.4;
		double balance = totalAmount - advance;

		cookBookingOneMeal.setBasePrice(basePrice);
		cookBookingOneMeal.setDishCharges(dishCharges);
		cookBookingOneMeal.setDiscount(discount);
		cookBookingOneMeal.setGst(gst);
		cookBookingOneMeal.setTotalAmount(totalAmount);
		cookBookingOneMeal.setAdvancePayment(advance);
		cookBookingOneMeal.setBalanceAmount(balance);

		return cookBookingRepo.save(cookBookingOneMeal);
	}

	private LocalDate calculateEndDate(LocalDate startDate, String duration) {
		switch (duration) {
		case "1 month":
			return startDate.plus(1, ChronoUnit.MONTHS);
		case "3 months":
			return startDate.plus(3, ChronoUnit.MONTHS);
		case "6 months":
			return startDate.plus(6, ChronoUnit.MONTHS);
		case "1 year":
			return startDate.plus(1, ChronoUnit.YEARS);
		default:
			throw new IllegalArgumentException("Invalid subscription duration");
		}
	}

	public CookBooking getBookingById(String cookBookingId) {
		return cookBookingRepo.findById(cookBookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found :" + cookBookingId));
	}

	public List<CookBooking> getAllBookings() {
		return cookBookingRepo.findAll();
	}

	@Transactional
	public void deleteBooking(String cookBookingId) {
		if (!cookBookingRepo.existsById(cookBookingId)) {
			throw new ResourceNotFoundException("Id not found :" + cookBookingId);
		}
		cookBookingRepo.deleteById(cookBookingId);
	}

	public CookBooking updateCookBooking(CookBooking cookBooking, String cookBookingId) {
		CookBooking updatedCookBooking = cookBookingRepo.findById(cookBookingId)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND: " + cookBookingId));

		if (Objects.nonNull(cookBooking.getOccasion()) && !"".equals(cookBooking.getOccasion())) {
			updatedCookBooking.setOccasion(cookBooking.getOccasion());
		}

		if (Objects.nonNull(cookBooking.getDate()) && !"".equals(cookBooking.getDate())) {
			updatedCookBooking.setDate(cookBooking.getDate());
		}

		if (Objects.nonNull(cookBooking.getMealType()) && !"".equals(cookBooking.getMealType())) {
			updatedCookBooking.setMealType(cookBooking.getMealType());
		}

		if (Objects.nonNull(cookBooking.getTimeSlot()) && !"".equals(cookBooking.getTimeSlot())) {
			updatedCookBooking.setTimeSlot(cookBooking.getTimeSlot());
		}

		if (cookBooking.getNumberOfPeople() > 0 && cookBooking.getNumberOfPeople() <= 10) {
			updatedCookBooking.setNumberOfPeople(cookBooking.getNumberOfPeople());
		}

		if (cookBooking.getGasBurners() > 0 && cookBooking.getGasBurners() <= 6) {
			updatedCookBooking.setGasBurners(cookBooking.getGasBurners());
		}

		if (Objects.nonNull(cookBooking.getLocation()) && !"".equals(cookBooking.getLocation())) {
			updatedCookBooking.setLocation(cookBooking.getLocation());
		}

		if (Objects.nonNull(cookBooking.getCuisine()) && !"".equals(cookBooking.getCuisine())) {
			updatedCookBooking.setCuisine(cookBooking.getCuisine());
		}

		if (Objects.nonNull(cookBooking.getDishes()) && !cookBooking.getDishes().isEmpty()) {
			updatedCookBooking.setDishes(cookBooking.getDishes());
		}

		if (cookBooking.getBasePrice() >= 0) {
			updatedCookBooking.setBasePrice(cookBooking.getBasePrice());
		}

		if (cookBooking.getDishCharges() >= 0) {
			updatedCookBooking.setDishCharges(cookBooking.getDishCharges());
		}

		if (cookBooking.getDiscount() >= 0) {
			updatedCookBooking.setDiscount(cookBooking.getDiscount());
		}

		if (cookBooking.getGst() >= 0) {
			updatedCookBooking.setGst(cookBooking.getGst());
		}

		if (cookBooking.getTotalAmount() >= 0) {
			updatedCookBooking.setTotalAmount(cookBooking.getTotalAmount());
		}

		if (cookBooking.getAdvancePayment() >= 0) {
			updatedCookBooking.setAdvancePayment(cookBooking.getAdvancePayment());
		}

		if (cookBooking.getBalanceAmount() >= 0) {
			updatedCookBooking.setBalanceAmount(cookBooking.getBalanceAmount());
		}

		return cookBookingRepo.save(updatedCookBooking);
	}

	private String generateCustomId() {
		String lastId = cookBookingRepo.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("ch")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "ch" + nextNumber;
	}

}
