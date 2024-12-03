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

		if (franchiseInquiry.getPhone() == null || franchiseInquiry.getPhone().isEmpty()) {
			throw new IllegalArgumentException("Phone is required");
		}
		return franchiseInquiryRepository.save(franchiseInquiry);
	}
}
