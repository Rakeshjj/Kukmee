package com.kukmee.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kukmee.Mailsender.EmailService;
import com.kukmee.Mailsender.EmailVerificationToken;
import com.kukmee.Mailsender.EmailVerificationTokenRepository;
import com.kukmee.entity.Chef;
import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.repository.ChefRepository;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.ChefSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private EmailVerificationTokenRepository emailVerificationTokenRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<?> registerChef(@Valid ChefSignup chefSignup) {
		// Check if username already exists
		if (chefRepository.existsByUsername(chefSignup.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}

		// Check if email already exists
		if (chefRepository.existsByEmail(chefSignup.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
		}

		// Check if phone number already exists
		if (chefRepository.existsByPhonenumber(chefSignup.getPhonenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use"));
		}

		// Create new Chef object and encode password
		Chef chef = new Chef(chefSignup.getUsername(), chefSignup.getEmail(), encoder.encode(chefSignup.getPassword()),
				chefSignup.getPhonenumber(), chefSignup.getYearsOfExperience(), chefSignup.getSpecialty());

		Set<String> strRoles = chefSignup.getRole();
		Set<Role> roles = new HashSet<>();

		// Assign default role if no roles are provided
		if (strRoles == null) {
			Role defaultRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(defaultRole);
		} else {
			strRoles.forEach(role -> {
				Role roleEntity = roleRepository.findByName(ERole.valueOf(role))
						.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
				roles.add(roleEntity);
			});
		}

		// Set roles and save Chef object
		chef.setRoles(roles);
		chefRepository.save(chef);

		// Generate verification token
		String token = UUID.randomUUID().toString();
		EmailVerificationToken verificationToken = new EmailVerificationToken();
		verificationToken.setToken(token);
		verificationToken.setChef(chef);
		verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token valid for 1 day
		emailVerificationTokenRepository.save(verificationToken);

		// Send verification email
		String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;
		emailService.sendSimpleMessage(chefSignup.getEmail(), "Email Verification",
				"Click the link to verify your email: " + verificationUrl);

		return ResponseEntity.ok(new MessageResponse("Chef registered successfully. Please verify your email."));
	}

	// Verify email via token
	public ResponseEntity<?> verifyEmail(String token) {
		Optional<EmailVerificationToken> verificationTokenOpt = emailVerificationTokenRepository.findByToken(token);
		if (!verificationTokenOpt.isPresent()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid or expired token"));
		}

		EmailVerificationToken verificationToken = verificationTokenOpt.get();
		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Token has expired"));
		}

		Chef chef = verificationToken.getChef();
		chef.setEmailVerified(true); // Mark email as verified
		chefRepository.save(chef);

		// Optionally, delete the token after use
		emailVerificationTokenRepository.delete(verificationToken);

		return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
	}

	// Bartender login (authentication)
	public ResponseEntity<?> authenticateChef(@Valid LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateToken(authentication);

			// Retrieve the user details
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Error: Invalid username or password"));
		}
	}

	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = chefRepository.findAll();
		return ResponseEntity.ok(chefs);
	}

	// Get Bartender by ID
	public ResponseEntity<?> getChefById(Long chefid) {
		Optional<Chef> chef = chefRepository.findById(chefid);
		if (chef.isPresent()) {
			return ResponseEntity.ok(chef.get());
		} else {
			return ResponseEntity.status(404).body(new MessageResponse("Bartender not found"));
		}
	}

	public ResponseEntity<?> updateChef(Long chefid, @Valid ChefSignup chefSignup) {
		Optional<Chef> chefOptional = chefRepository.findById(chefid);

		if (!chefOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Bartender not found"));
		}

		Chef chef = chefOptional.get();
		chef.setUsername(chefSignup.getUsername());
		chef.setEmail(chefSignup.getEmail());
		chef.setPhonenumber(chefSignup.getPhonenumber());
		chef.setYearsOfExperience(chefSignup.getYearsOfExperience());
		chef.setSpecialty(chefSignup.getSpecialty());
		chef.setPassword(encoder.encode(chefSignup.getPassword())); // Update password if needed

		Set<String> strRoles = chefSignup.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles != null) {
			strRoles.forEach(role -> {
				switch (role) {
				case "bartender":
					Role bartenderRole = roleRepository.findByName(ERole.ROLE_BARTENDER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(bartenderRole);
					break;

				case "chef":
					Role chefRole = roleRepository.findByName(ERole.ROLE_CHEF)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(chefRole);
					break;

				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}
		chef.setRoles(roles);

		chefRepository.save(chef);
		return ResponseEntity.ok(new MessageResponse("Bartender information updated successfully"));
	}

	// Delete Bartender
	public ResponseEntity<?> deleteChef(Long chefid) {
		Optional<Chef> chefOptional = chefRepository.findById(chefid);
		if (!chefOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Bartender not found"));
		}

		chefRepository.deleteById(chefid);
		return ResponseEntity.ok(new MessageResponse("Bartender deleted successfully"));
	}

}
