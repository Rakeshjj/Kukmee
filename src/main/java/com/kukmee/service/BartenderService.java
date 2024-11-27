package com.kukmee.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

import com.kukmee.entity.Bartender;
import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.repository.BartenderRepository;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.BartenderSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@Service
public class BartenderService {

	@Autowired
	private BartenderRepository bartenderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<?> registerBartender(@Valid BartenderSignup bartenderSignup) {
		// Check if username already exists
		if (bartenderRepository.existsByUsername(bartenderSignup.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}

		// Check if email already exists
		if (bartenderRepository.existsByEmail(bartenderSignup.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
		}

		// Check if phone number already exists
		if (bartenderRepository.existsByPhonenumber(bartenderSignup.getPhonenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use"));
		}

		// Now, proceed with saving the bartender
		Bartender bartender = new Bartender(bartenderSignup.getUsername(), bartenderSignup.getEmail(),
				encoder.encode(bartenderSignup.getPassword()), bartenderSignup.getPhonenumber(),
				bartenderSignup.getBartenderExperience(), bartenderSignup.getDrinkSpecialty());

		Set<String> strRoles = bartenderSignup.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			// Default role to customer
			Role defaultRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(defaultRole);
		} else {
			strRoles.forEach(role -> {
				if (role.equalsIgnoreCase("ROLE_BARTENDER")) {
					Role bartenderRole = roleRepository.findByName(ERole.ROLE_BARTENDER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(bartenderRole);
				} else {
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}

		bartender.setRoles(roles);
		bartenderRepository.save(bartender);

		return ResponseEntity.ok(new MessageResponse("Bartender Registered Successfully"));
	}

	// Bartender login (authentication)
	public ResponseEntity<?> authenticateBartender(@Valid LoginRequest loginRequest) {
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

	// Get all bartenders
	public ResponseEntity<?> getAllBartenders() {
		List<Bartender> bartenders = bartenderRepository.findAll();
		return ResponseEntity.ok(bartenders);
	}

	// Get Bartender by ID
	public ResponseEntity<?> getBartenderById(Long bartenderid) {
		Optional<Bartender> bartender = bartenderRepository.findById(bartenderid);
		if (bartender.isPresent()) {
			return ResponseEntity.ok(bartender.get());
		} else {
			return ResponseEntity.status(404).body(new MessageResponse("Bartender not found"));
		}
	}

	// Update Bartender information
	public ResponseEntity<?> updateBartender(Long bartenderid, @Valid BartenderSignup bartenderSignup) {
		Optional<Bartender> bartenderOptional = bartenderRepository.findById(bartenderid);

		if (!bartenderOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Bartender not found"));
		}

		Bartender bartender = bartenderOptional.get();
		// Update bartender's information
		bartender.setUsername(bartenderSignup.getUsername());
		bartender.setEmail(bartenderSignup.getEmail());
		bartender.setPhonenumber(bartenderSignup.getPhonenumber());
		bartender.setBartenderExperience(bartenderSignup.getBartenderExperience());
		bartender.setDrinkSpecialty(bartenderSignup.getDrinkSpecialty());
		bartender.setPassword(encoder.encode(bartenderSignup.getPassword())); // Update password if needed

		// Handle roles if needed (can be modified as per requirements)
		Set<String> strRoles = bartenderSignup.getRole();
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
		bartender.setRoles(roles);

		bartenderRepository.save(bartender);
		return ResponseEntity.ok(new MessageResponse("Bartender information updated successfully"));
	}

	// Delete Bartender
	public ResponseEntity<?> deleteBartender(Long bartenderid) {
		Optional<Bartender> bartenderOptional = bartenderRepository.findById(bartenderid);
		if (!bartenderOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Bartender not found"));
		}

		bartenderRepository.deleteById(bartenderid);
		return ResponseEntity.ok(new MessageResponse("Bartender deleted successfully"));
	}
}
