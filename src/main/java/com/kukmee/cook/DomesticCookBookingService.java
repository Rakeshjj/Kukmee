package com.kukmee.cook;

import org.springframework.stereotype.Service;

@Service
public class DomesticCookBookingService {

	private final DomesticCookBookingRepository domesticCookBookingRepository;

	public DomesticCookBookingService(DomesticCookBookingRepository domesticCookBookingRepository) {
		super();
		this.domesticCookBookingRepository = domesticCookBookingRepository;
	}

	public DomesticCookBooking createDomesticCookBooking(DomesticCookBooking domesticCookBooking) {

		if (domesticCookBooking.getNumberOfPeople() >= 8) {
			throw new IllegalArgumentException("Maximum 8 people allowed");
		}

		if (domesticCookBooking.getNumberOfPeople() <= 0) {
			throw new IllegalArgumentException("Number of people must be greater than zero");
		}

		double serviceCharge = 169;

		if (domesticCookBooking.getNumberOfPeople() > 4) {
			int additionalPeople = domesticCookBooking.getNumberOfPeople() - 4;
			int additionalCharges = (int) Math.ceil(additionalPeople / 4.0) * 150;
			serviceCharge += additionalCharges;
		}

		int dishcount = 4;
		double dishCharges = dishcount * 199;

		double couponDiscount = 132;

		double subtotal = serviceCharge + dishCharges - couponDiscount;

		double gst = subtotal * 0.18;

		double totalAmount = subtotal + gst;

		double advancePayment = totalAmount * 0.4;

		double balanceAmount = totalAmount - advancePayment;

		domesticCookBooking.setTotalAmount(totalAmount);
		domesticCookBooking.setAdvancePayment(advancePayment);
		domesticCookBooking.setBalanceAmount(balanceAmount);

		return domesticCookBookingRepository.save(domesticCookBooking);

	}

}