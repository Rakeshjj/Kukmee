package com.kukmee.franchise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/franchise")
public class FranchiseInquiryController {

	@Autowired
	private FranchiseInquiryService franchiseInquiryService;

	@PostMapping("/inquiry")
	public ResponseEntity<?> createInquiry(@RequestBody FranchiseInquiry franchiseInquiry) {
		franchiseInquiryService.saveInquiry(franchiseInquiry);
		return ResponseEntity.ok("Inquiry send successfully...");

	}
}
