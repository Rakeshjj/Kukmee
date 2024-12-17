package com.kukmee.chef.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.chef.ChefBooking;
import com.kukmee.chef.ChefBookingMultipleDays;
import com.kukmee.chef.repo.ChefBookingMultipleDaysRepo;
import com.kukmee.exception.ResourceNotFoundException;

@Service
public class ChefbookingMultipleDaysService {

	@Autowired
	private ChefBookingMultipleDaysRepo chefBookingMultipleDaysRepo;

	public ChefBookingMultipleDays createBooking(ChefBookingMultipleDays chefBooking) {

		String newId = generateCustomId();
		chefBooking.setChefDayId(newId);

		if (chefBooking.getOccasion() == null || chefBooking.getOccasion().isEmpty()) {
			throw new IllegalArgumentException("Occasion is required");
		}

		if (chefBooking.getDate() == null || chefBooking.getDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (chefBooking.getLocation() == null || chefBooking.getLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (chefBooking.getMealType() == null || chefBooking.getMealType().isEmpty()) {
			throw new IllegalArgumentException("Meal type (Breakfast, Lunch, Dinner) is required.");
		}

		if ((chefBooking.getMealType().equalsIgnoreCase("lunch")
				|| chefBooking.getMealType().equalsIgnoreCase("dinner"))
				&& (chefBooking.getCuisine() == null || chefBooking.getCuisine().isEmpty())) {
			throw new IllegalArgumentException("Cuisine is required for lunch and dinner bookings.");
		}

		if (chefBooking.getMealType().equalsIgnoreCase("breakfast")) {
			chefBooking.setCuisine(null);
		}

		int people = chefBooking.getNumberOfPeople();
		double basePrice = 219 + ((people - 1) / 4) * 200;

		double dishCharges = chefBooking.getDishes().size() * 200;

		double discount = 0;
		if (chefBooking.getDishes().size() > 5) {
			discount = 100; // Adjust discount threshold as needed
		}

		double subtotal = basePrice + dishCharges - discount;

		double gst = subtotal * 0.18;

		double totalAmount = subtotal + gst;
		double advance = totalAmount * 0.4;
		double balance = totalAmount - advance;

		chefBooking.setBasePrice(basePrice);
		chefBooking.setDishCharges(dishCharges);
		chefBooking.setDiscount(discount);
		chefBooking.setGst(gst);
		chefBooking.setTotalAmount(totalAmount);
		chefBooking.setAdvancePayment(advance);
		chefBooking.setBalanceAmount(balance);

		return chefBookingMultipleDaysRepo.save(chefBooking);
	}

	public List<ChefBookingMultipleDays> getAllBookings() {
		return chefBookingMultipleDaysRepo.findAll();
	}

	public ChefBookingMultipleDays getById(String chefDayId) {
		return chefBookingMultipleDaysRepo.findById(chefDayId)
				.orElseThrow(() -> new ResourceNotFoundException("ID Not Found:" + chefDayId));
	}

	public void deleteById(String chefDayId) {
		if (!chefBookingMultipleDaysRepo.existsById(chefDayId)) {
			throw new ResourceNotFoundException("ID Not Found:" + chefDayId);
		}
		chefBookingMultipleDaysRepo.deleteById(chefDayId);
	}

	public ChefBookingMultipleDays updateChefBooking(ChefBookingMultipleDays chefBooking, String chefDayId) {
		ChefBookingMultipleDays chefBookingUpdate = chefBookingMultipleDaysRepo.findById(chefDayId)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND:" + chefDayId));

		if (Objects.nonNull(chefBooking.getOccasion()) && !"".equals(chefBooking.getOccasion())) {
			chefBookingUpdate.setOccasion(chefBooking.getOccasion());
		}

		if (Objects.nonNull(chefBooking.getDate()) && !"".equals(chefBooking.getDate())) {
			chefBookingUpdate.setDate(chefBooking.getDate());
		}

		if (Objects.nonNull(chefBooking.getMealType()) && !"".equals(chefBooking.getMealType())) {
			chefBookingUpdate.setMealType(chefBooking.getMealType());
		}

		if (Objects.nonNull(chefBooking.getTimeSlot()) && !"".equals(chefBooking.getTimeSlot())) {
			chefBookingUpdate.setTimeSlot(chefBooking.getTimeSlot());
		}

		if (chefBooking.getNumberOfPeople() > 0) {
			chefBookingUpdate.setNumberOfPeople(chefBooking.getNumberOfPeople());
		}

		if (chefBooking.getGasBurners() > 0) {
			chefBookingUpdate.setGasBurners(chefBooking.getGasBurners());
		}

		if (Objects.nonNull(chefBooking.getLocation()) && !"".equals(chefBooking.getLocation())) {
			chefBookingUpdate.setLocation(chefBooking.getLocation());
		}

		if (chefBooking.getDishes() != null && !chefBooking.getDishes().isEmpty()) {
			chefBookingUpdate.setDishes(chefBooking.getDishes());
		}

		if (Objects.nonNull(chefBooking.getCuisine()) && !"".equals(chefBooking.getCuisine())) {
			chefBookingUpdate.setCuisine(chefBooking.getCuisine());
		}

		if (chefBooking.getBasePrice() >= 0) {
			chefBookingUpdate.setBasePrice(chefBooking.getBasePrice());
		}

		if (chefBooking.getDishCharges() >= 0) {
			chefBookingUpdate.setDishCharges(chefBooking.getDishCharges());
		}

		if (chefBooking.getDiscount() >= 0) {
			chefBookingUpdate.setDiscount(chefBooking.getDiscount());
		}

		if (chefBooking.getGst() >= 0) {
			chefBookingUpdate.setGst(chefBooking.getGst());
		}

		if (chefBooking.getTotalAmount() >= 0) {
			chefBookingUpdate.setTotalAmount(chefBooking.getTotalAmount());
		}

		if (chefBooking.getAdvancePayment() >= 0) {
			chefBookingUpdate.setAdvancePayment(chefBooking.getAdvancePayment());
		}

		if (chefBooking.getBalanceAmount() >= 0) {
			chefBookingUpdate.setBalanceAmount(chefBooking.getBalanceAmount());
		}

		return chefBookingMultipleDaysRepo.save(chefBookingUpdate);
	}

	private String generateCustomId() {
		String lastId = chefBookingMultipleDaysRepo.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("mu")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "mu" + nextNumber;
	}
}
