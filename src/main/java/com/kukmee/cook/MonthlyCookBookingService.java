package com.kukmee.cook;

import org.springframework.stereotype.Service;

@Service
public class MonthlyCookBookingService {

	private final MonthlyCookBookingRepository monthlyCookBookingRepository;

	public MonthlyCookBookingService(MonthlyCookBookingRepository monthlyCookBookingRepository) {
		super();
		this.monthlyCookBookingRepository = monthlyCookBookingRepository;
	}

	public MonthlyCookBooking createBooking(MonthlyCookBooking monthlyCookBooking, double couponDiscount) {

		if (monthlyCookBooking.getNumberOfPeople() <= 0 || monthlyCookBooking.getNumberOfPeople() > 6) {
			throw new IllegalArgumentException("Number of people must be between 1 and 6");
		}

		double baseCharge = 3069;

		switch (monthlyCookBooking.getNumberOfPeople()) {
		case 2: {
			baseCharge += 100;
			break;
		}
		case 3: {
			baseCharge += 150;
			break;
		}
		case 4: {
			baseCharge += 500;
			break;
		}
		case 5: {
			baseCharge += 800;
			break;
		}
		case 6: {
			baseCharge += 1000;
			break;
		}

		default:
			throw new IllegalArgumentException("Unexpected value: " + baseCharge);
		}

		double discountAmount = baseCharge - couponDiscount;

		double gst = discountAmount * 0.18;

		double toltalAmount = discountAmount + gst;

		double advancepayment = toltalAmount * 0.4;
		double balanceAmount = toltalAmount - advancepayment;

		monthlyCookBooking.setAdvancePayment(advancepayment);
		monthlyCookBooking.setBalanceAmount(balanceAmount);
		monthlyCookBooking.setTotalAmount(toltalAmount);

		return monthlyCookBookingRepository.save(monthlyCookBooking);
	}
}
