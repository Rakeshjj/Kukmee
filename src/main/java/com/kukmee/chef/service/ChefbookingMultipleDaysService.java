package com.kukmee.chef.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.chef.ChefBookingMultipleDays;
import com.kukmee.chef.repo.ChefBookingMultipleDaysRepo;

@Service
public class ChefbookingMultipleDaysService {

	@Autowired
	private ChefBookingMultipleDaysRepo chefBookingMultipleDaysRepo;

	public ChefBookingMultipleDays createBooking(ChefBookingMultipleDays chefBooking) {

		String newId = generateCustomId();
		chefBooking.setChefDayId(newId);;

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

	private String generateCustomId() {
		String lastId = chefBookingMultipleDaysRepo.findLastBookingId();

		int nextNumber = 1;
		if (lastId != null && lastId.startsWith("chmd")) {
			nextNumber = Integer.parseInt(lastId.substring(2)) + 1;
		}

		return "chmd" + nextNumber;
	}
}
