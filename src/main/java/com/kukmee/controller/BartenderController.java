package com.kukmee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.request.BartenderSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.service.BartenderService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class BartenderController {

	@Autowired
	private BartenderService bartenderService;

	@PostMapping("/bartendersignup")
	public ResponseEntity<?> registerBartender(@Valid @RequestBody BartenderSignup bartender) {
		return bartenderService.registerBartender(bartender);
	}

	@PostMapping("/bartendersignin")
	public ResponseEntity<?> authenticateBartender(@Valid @RequestBody LoginRequest loginRequest) {
		return bartenderService.authenticateBartender(loginRequest);
	}

	@GetMapping("/bartenderall")
	public ResponseEntity<?> getAllBartenders() {
		return bartenderService.getAllBartenders();
	}

	@GetMapping("/{bartenderid}")
	public ResponseEntity<?> getBartenderById(@PathVariable Long bartenderid) {
		return bartenderService.getBartenderById(bartenderid);
	}

	@PutMapping("/bartender/{bartenderid}")
	public ResponseEntity<?> updateBartender(@PathVariable Long bartenderid,
			@Valid @RequestBody BartenderSignup bartenderSignup) {
		return bartenderService.updateBartender(bartenderid, bartenderSignup);
	}

	@DeleteMapping("/{bartenderid}")
	public ResponseEntity<?> deleteBartender(@PathVariable Long bartenderid) {
		return bartenderService.deleteBartender(bartenderid);
	}
}
