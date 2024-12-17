package com.kukmee.cook.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.cook.CookBookingOneMeal;
import com.kukmee.cook.repo.CookBookingRepoOneMeal;
import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CookBookingServiceOneMeal {

	@Autowired
	private CookBookingRepoOneMeal cookBookingRepoOneMeal;

	public CookBookingOneMeal createBooking(CookBookingOneMeal cookBookingOneMeal) {

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

		if (cookBookingOneMeal.getMealType().equalsIgnoreCase("breakfast")) {
			cookBookingOneMeal.setCuisine(null);
		}

		int people = cookBookingOneMeal.getNumberOfPeople();
		double basePrice = 219 + ((people - 1) / 4) * 200;

		double dishCharges = cookBookingOneMeal.getDishes().size() * 200;

		double discount = 0;
		if (cookBookingOneMeal.getDishes().size() > 5) {
			discount = 100; // Adjust discount threshold as needed
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

		return cookBookingRepoOneMeal.save(cookBookingOneMeal);
	}

	public CookBookingOneMeal getBookingById(String cookBookingId) {
		return cookBookingRepoOneMeal.findById(cookBookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found :" + cookBookingId));
	}

	public List<CookBookingOneMeal> getAllBookings() {
		return cookBookingRepoOneMeal.findAll();
	}

	@Transactional
	public void deleteBooking(String cookBookingId) {
		if (!cookBookingRepoOneMeal.existsById(cookBookingId)) {
			throw new ResourceNotFoundException("Id not found :" + cookBookingId);
		}
		cookBookingRepoOneMeal.deleteById(cookBookingId);
	}

	public CookBookingOneMeal updateCookBooking(CookBookingOneMeal cookBooking, String cookBookingId) {
		CookBookingOneMeal updatedCookBooking = cookBookingRepoOneMeal.findById(cookBookingId)
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

		return cookBookingRepoOneMeal.save(updatedCookBooking);
	}

	private String generateCustomId() {
		String lastId = cookBookingRepoOneMeal.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("ch")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "ch" + nextNumber;
	}

}
