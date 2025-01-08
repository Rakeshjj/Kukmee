package com.kukmee.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;
import com.kukmee.request.ForgotPasswordRequest;
import com.kukmee.request.ResetPasswordRequest;
import com.kukmee.service.ForgotPasswordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		Customer customer = customerRepository.findByEmail(request.getEmail());
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found with the provided email.");
		}

		forgotPasswordService.generatePasswordResetToken(request.getEmail());
		String resetToken = customer.getResetToken();
		forgotPasswordService.sendResetPasswordEmail(request.getEmail(), resetToken);

		return ResponseEntity.ok("Password reset token has been sent to your email.");
	}

	@GetMapping("/reset-password")
	public ResponseEntity<?> resetPasswordTokenHandler(@RequestParam String token) throws JsonProcessingException {
		try {
			System.out.println("Received token: " + token);

			Customer customer = customerRepository.findByResetToken(token);
			if (customer == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ObjectMapper().writeValueAsString("Invalid token."));
			}

			if (customer.getResetTokenExpirationTime().isBefore(LocalDateTime.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ObjectMapper().writeValueAsString("Token has expired."));
			}

			return ResponseEntity.ok(
					new ObjectMapper().writeValueAsString("Token is valid. You may proceed to reset your password."));

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ObjectMapper().writeValueAsString("An unexpected error occurred."));
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
		Customer customer = customerRepository.findByResetToken(token);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
		}

		if (customer.getResetTokenExpirationTime().isBefore(LocalDateTime.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
		}

		// Reset the password here
		customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
		customer.setResetToken(null); // Clear the token after use
		customerRepository.save(customer);

		return ResponseEntity.ok("Password has been successfully reset.");
	}

}
