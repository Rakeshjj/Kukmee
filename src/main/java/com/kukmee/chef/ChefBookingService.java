package com.kukmee.chef;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ChefBookingService {

	@Autowired
	private ChefBookingRepository chefBookingRepository;

	public ChefBooking createBooking(ChefBooking chefBooking) {

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

		return chefBookingRepository.save(chefBooking);
	}

	public ChefBooking getBookingById(Long id) {
		return chefBookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found :" + id));
	}

	public List<ChefBooking> getAllBookings() {
		return chefBookingRepository.findAll();
	}

	@Transactional
	public void deleteBooking(Long id) {
		if (!chefBookingRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found :" + id);
		}
		chefBookingRepository.deleteById(id);
	}

	@Transactional
	public ChefBooking updateBooking(Long id, ChefBooking chefBooking) {
		if (chefBookingRepository.existsById(id)) {
			chefBooking.setId(id);
			return chefBookingRepository.save(chefBooking);
		} else {
			throw new ResourceNotFoundException("id not found :" + id);
		}
	}
}
