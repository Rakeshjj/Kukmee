package com.kukmee.cook.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.cook.CookBookingMultipleDays;
import com.kukmee.cook.CookBookingOneMeal;
import com.kukmee.cook.repo.CookBookingRepoMultipleDays;
import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CookbookingMultipleDaysService {

	@Autowired
	private CookBookingRepoMultipleDays cookBookingRepoMultipleDays;

	public CookBookingMultipleDays createBooking(CookBookingMultipleDays cookBooking) {

		String newId = generateCustomId();
		cookBooking.setCookDayId(newId);

		if (cookBooking.getOccasion() == null || cookBooking.getOccasion().isEmpty()) {
			throw new IllegalArgumentException("Occasion is required");
		}

		if (cookBooking.getDate() == null || cookBooking.getDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (cookBooking.getLocation() == null || cookBooking.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (cookBooking.getMealType() == null || cookBooking.getMealType().isEmpty()) {
			throw new IllegalArgumentException("Meal type (Breakfast, Lunch, Dinner) is required.");
		}

		if ((cookBooking.getMealType().equalsIgnoreCase("lunch")
				|| cookBooking.getMealType().equalsIgnoreCase("dinner"))
				&& (cookBooking.getCuisine() == null || cookBooking.getCuisine().isEmpty())) {
			throw new IllegalArgumentException("Cuisine is required for lunch and dinner bookings.");
		}

		if (cookBooking.getMealType().equalsIgnoreCase("breakfast")) {
			cookBooking.setCuisine(null);
		}

		int people = cookBooking.getNumberOfPeople();
		double basePrice = 219 + ((people - 1) / 4) * 200;

		double dishCharges = cookBooking.getDishes().size() * 200;

		double discount = 0;
		if (cookBooking.getDishes().size() > 5) {
			discount = 100; // Adjust discount threshold as needed
		}

		double subtotal = basePrice + dishCharges - discount;

		double gst = subtotal * 0.18;

		double totalAmount = subtotal + gst;
		double advance = totalAmount * 0.4;
		double balance = totalAmount - advance;

		cookBooking.setBasePrice(basePrice);
		cookBooking.setDishCharges(dishCharges);
		cookBooking.setDiscount(discount);
		cookBooking.setGst(gst);
		cookBooking.setTotalAmount(totalAmount);
		cookBooking.setAdvancePayment(advance);
		cookBooking.setBalanceAmount(balance);

		return cookBookingRepoMultipleDays.save(cookBooking);
	}
	
	public CookBookingMultipleDays getBookingById(String cookDayId) {
		return cookBookingRepoMultipleDays.findById(cookDayId)
				.orElseThrow(() -> new ResourceNotFoundException("Id Not Found :" + cookDayId));
	}

	public List<CookBookingMultipleDays> getAllBookings() {
		return cookBookingRepoMultipleDays.findAll();
	}

	@Transactional
	public void deleteBooking(String cookDayId) {
		if (!cookBookingRepoMultipleDays.existsById(cookDayId)) {
			throw new ResourceNotFoundException("Id Not Found :" + cookDayId);
		}
		cookBookingRepoMultipleDays.deleteById(cookDayId);
	}
	
	public CookBookingMultipleDays updateCookBooking(CookBookingMultipleDays cookBooking, String cookDayId) {
		CookBookingMultipleDays updatedCookBooking = cookBookingRepoMultipleDays.findById(cookDayId)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND: " + cookDayId));

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

		return cookBookingRepoMultipleDays.save(updatedCookBooking);
	}

	private String generateCustomId() {
		String lastId = cookBookingRepoMultipleDays.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("mu")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "mu" + nextNumber;
	}
}
