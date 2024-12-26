package com.kukmee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.request.CookSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.service.CookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class CookController {

	@Autowired
	private CookService cookService;

	// Register a new cook
	@PostMapping("/cooksignup")
	public ResponseEntity<?> registerCook(@Valid @RequestBody CookSignup cookSignup) {
		return cookService.registerCook(cookSignup);
	}

	// Cook login (authentication)
	@PostMapping("/cooksignin")
	public ResponseEntity<?> authenticateCook(@Valid @RequestBody LoginRequest loginRequest) {
		return cookService.authenticateCook(loginRequest);
	}

	// Get all cooks
	@GetMapping("/getAllCooks")
	public ResponseEntity<?> getAllCooks() {
		return cookService.getAllCooks();
	}

	// Get cook by ID
	@GetMapping("/cook/{cookid}")
	public ResponseEntity<?> getCookById(@PathVariable Long cookid) {
		return cookService.getCookById(cookid);
	}

	// Update cook information
	@PutMapping("/cook/{cookid}")
	public ResponseEntity<?> updateCook(@PathVariable Long cookid, @Valid @RequestBody CookSignup cookSignup) {
		return cookService.updateCook(cookid, cookSignup);
	}

	// Delete cook
	@DeleteMapping("/cook/{cookid}")
	public ResponseEntity<?> deleteCook(@PathVariable Long cookid) {
		return cookService.deleteCook(cookid);
	}
}
