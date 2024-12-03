package com.kukmee.cook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyCookBookingService {

	@Autowired
	private  MonthlyCookBookingRepository bookingRepository;


	public MonthlyCookBooking createBooking(MonthlyCookBooking booking, double couponDiscount) {

		if (booking.getNumberOfPeople() <= 0 || booking.getNumberOfPeople() > 6) {
			throw new IllegalArgumentException("Number of people must be between 1 and 6.");
		}

		double baseCharge = 3069;

		switch (booking.getNumberOfPeople()) {
		case 2:
			baseCharge += 100;
			break;
		case 3:
			baseCharge += 150;
			break;
		case 4:
			baseCharge += 500;
			break;
		case 5:
			baseCharge += 800;
			break;
		case 6:
			baseCharge += 1000;
			break;
		}

		double discountedAmount = baseCharge - couponDiscount;

		double gst = discountedAmount * 0.18;

		double totalAmount = discountedAmount + gst;

		double advancePayment = totalAmount * 0.4;
		double balanceAmount = totalAmount - advancePayment;

		booking.setTotalAmount(totalAmount);
		booking.setAdvancePayment(advancePayment);
		booking.setBalanceAmount(balanceAmount);

		return bookingRepository.save(booking);
	}

	public List<MonthlyCookBooking> getAllBookings() {
		List<MonthlyCookBooking> monthlyCookBookings = bookingRepository.findAll();
		return monthlyCookBookings;
	}
}
