package com.kukmee.catering;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.exception.ResourceNotFoundException;

@Service
public class CateringBookingService {

	@Autowired
	private CateringBookingRepository cateringBookingRepository;

	public CateringBooking createCateringBooking(CateringBooking cateringBooking) {

		if (cateringBooking.getOccasion() == null || cateringBooking.getOccasion().isEmpty()) {
			throw new IllegalArgumentException("Occasion is required");
		}

		if (cateringBooking.getEventDate() == null || cateringBooking.getEventDate().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (cateringBooking.getVenueLocation() == null || cateringBooking.getVenueLocation().isEmpty()) {
			throw new IllegalArgumentException("Location is required");
		}

		if (cateringBooking.getNumberOfPeople() <= 0) {
			throw new IllegalArgumentException("Number of people must be greater than zero");
		}

		if (cateringBooking.getPhoneNumber() == null
				|| String.valueOf(cateringBooking.getPhoneNumber()).length() != 10) {
			throw new IllegalArgumentException("Phone number is required & exactly 10 digits");
		}

		double serviceCharge = 500;
		if (cateringBooking.getNumberOfPeople() > 50) {
			serviceCharge += 150 * (cateringBooking.getNumberOfPeople() - 50) / 10;
		}

		double reducedMenuPricePerDish = 180;
		int menuDishCount = cateringBooking.getSelectedMenu().size();
		double menuCharge = reducedMenuPricePerDish * menuDishCount * cateringBooking.getNumberOfPeople();

		double subtotal = serviceCharge + menuCharge;

		double gst = subtotal * 0.18;

		double totalAmount = subtotal + gst;

		cateringBooking.setServiceCharge(serviceCharge);
		cateringBooking.setMenuCharge(menuCharge);
		cateringBooking.setGst(gst);
		cateringBooking.setTotalAmount(totalAmount);

		return cateringBookingRepository.save(cateringBooking);
	}

	public List<CateringBooking> getAllBookings() {
		return cateringBookingRepository.findAll();
	}

	public CateringBooking getById(Long cateringId) {
		return cateringBookingRepository.findById(cateringId)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found :" + cateringId));
	}

	public void deleteCateringBooking(Long cateringId) {
		if (!cateringBookingRepository.existsById(cateringId)) {
			throw new ResourceNotFoundException("Id not found :" + cateringId);
		}
		cateringBookingRepository.deleteById(cateringId);
	}

	public CateringBooking updateCateringBooking(CateringBooking cateringBooking, Long cateringId) {
		CateringBooking cateringBookingUpdate = cateringBookingRepository.findById(cateringId)
				.orElseThrow(() -> new RuntimeException("Catering booking not found"));

		if (Objects.nonNull(cateringBooking.getOccasion()) && !cateringBooking.getOccasion().isEmpty()) {
			cateringBookingUpdate.setOccasion(cateringBooking.getOccasion());
		}

		if (Objects.nonNull(cateringBooking.getEventDate()) && !cateringBooking.getEventDate().isEmpty()) {
			cateringBookingUpdate.setEventDate(cateringBooking.getEventDate());
		}

		if (Objects.nonNull(cateringBooking.getEventTime()) && !cateringBooking.getEventTime().isEmpty()) {
			cateringBookingUpdate.setEventTime(cateringBooking.getEventTime());
		}

		if (cateringBooking.getNumberOfPeople() > 0) {
			cateringBookingUpdate.setNumberOfPeople(cateringBooking.getNumberOfPeople());
		}

		if (Objects.nonNull(cateringBooking.getCuisine()) && !cateringBooking.getCuisine().isEmpty()) {
			cateringBookingUpdate.setCuisine(cateringBooking.getCuisine());
		}

		if (Objects.nonNull(cateringBooking.getPhoneNumber())) {
			cateringBookingUpdate.setPhoneNumber(cateringBooking.getPhoneNumber());
		}

		if (Objects.nonNull(cateringBooking.getVenueLocation()) && !cateringBooking.getVenueLocation().isEmpty()) {
			cateringBookingUpdate.setVenueLocation(cateringBooking.getVenueLocation());
		}

		if (cateringBooking.getServiceCharge() >= 0) {
			cateringBookingUpdate.setServiceCharge(cateringBooking.getServiceCharge());
		}

		if (cateringBooking.getMenuCharge() >= 0) {
			cateringBookingUpdate.setMenuCharge(cateringBooking.getMenuCharge());
		}

		if (cateringBooking.getGst() >= 0) {
			cateringBookingUpdate.setGst(cateringBooking.getGst());
		}

		if (cateringBooking.getTotalAmount() >= 0) {
			cateringBookingUpdate.setTotalAmount(cateringBooking.getTotalAmount());
		}

		if (Objects.nonNull(cateringBooking.getSelectedMenu()) && !cateringBooking.getSelectedMenu().isEmpty()) {
			cateringBookingUpdate.setSelectedMenu(cateringBooking.getSelectedMenu());
		}

		return cateringBookingRepository.save(cateringBookingUpdate);
	}

}
