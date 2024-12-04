package com.kukmee.franchise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FranchiseInquiryService {

	@Autowired
	private FranchiseInquiryRepository franchiseInquiryRepository;

	public FranchiseInquiry saveInquiry(FranchiseInquiry franchiseInquiry) {

		if (franchiseInquiry.getName() == null || franchiseInquiry.getName().isEmpty()) {
			throw new IllegalArgumentException("Name is required");
		}

		if (franchiseInquiry.getEmail() == null || franchiseInquiry.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		}

		if (franchiseInquiry.getPhone() == null || String.valueOf(franchiseInquiry.getPhone()).length() != 10) {
			throw new IllegalArgumentException("Phone number is required & exactly 10 digits");
		}
		return franchiseInquiryRepository.save(franchiseInquiry);
	}
}
