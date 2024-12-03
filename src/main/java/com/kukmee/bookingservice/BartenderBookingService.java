package com.kukmee.bookingservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.bookingentity.AmountDetails;
import com.kukmee.bookingentity.BartenderBooking;
import com.kukmee.bookingentity.Drink;
import com.kukmee.bookingrepository.AmountDetailsRepository;
import com.kukmee.bookingrepository.BartenderBookingRepository;
import com.kukmee.exception.ResourceNotFoundException;

@Service
public class BartenderBookingService {

	@Autowired
	private BartenderBookingRepository bartenderBookingRepository;

	@Autowired
	private AmountDetailsRepository amountDetailsRepository;

	// Create booking
	public BartenderBooking createBooking(BartenderBooking booking) {
		// Ensure each selected drink has the booking associated with it
		for (Drink drink : booking.getSelectedDrinks()) {
			drink.setBooking(booking);
		}

		AmountDetails amountDetails = booking.getAmountDetails();
		if (amountDetails != null) {
			amountDetails.setBooking(booking);
		}
		return bartenderBookingRepository.save(booking);
	}

	public void updatePaymentDetails(Long bookingid, double remainingPayment) {
		BartenderBooking bartenderBooking = bartenderBookingRepository.findById(bookingid)
				.orElseThrow(() -> new ResourceNotFoundException("Booking id not found"));

		AmountDetails amountDetails = bartenderBooking.getAmountDetails();
		if (amountDetails == null) {
			throw new ResourceNotFoundException("Amount details not found for the booking");
		}

		amountDetails.setAdvancePayment(amountDetails.getTotalAmount());
		amountDetails.setBalanceAmount(0.0);

		// If you want to track the exact remaining payment, you can update the balance
		amountDetails.setBalanceAmount(amountDetails.getBalanceAmount() - remainingPayment);

		// Save updated payment details
		amountDetailsRepository.save(amountDetails);
	}

	// Retrieve all bartender bookings
	public List<BartenderBooking> getAllBartenderBookings() {
		return bartenderBookingRepository.findAll();
	}
}
