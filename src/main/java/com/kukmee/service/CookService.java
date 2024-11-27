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

import com.kukmee.entity.Cook;
import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.exception.ResourceNotFoundException;
import com.kukmee.repository.CookRepository;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.CookSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@Service
public class CookService {

	@Autowired
	private CookRepository cookRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	// Register a new cook
	public ResponseEntity<?> registerCook(@Valid CookSignup cookSignup) {

		if (cookRepository.existsByUsername(cookSignup.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}

		// Check if email already exists
		if (cookRepository.existsByEmail(cookSignup.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
		}

		// Check if phone number already exists
		if (cookRepository.existsByPhonenumber(cookSignup.getPhonenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use"));
		}

		Cook cook = new Cook(cookSignup.getUsername(), cookSignup.getEmail(), encoder.encode(cookSignup.getPassword()),
				cookSignup.getPhonenumber(), cookSignup.getCookExperienceLevel(), cookSignup.getCookSpecialty());

		Set<String> strRoles = cookSignup.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			// Default role to cook
			Role defaultRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(defaultRole);
		} else {
			strRoles.forEach(role -> {
				if (role.equalsIgnoreCase("ROLE_COOK")) {
					Role cookRole = roleRepository.findByName(ERole.ROLE_COOK)
							.orElseThrow(() -> new RuntimeException("Error : role is not found"));
					roles.add(cookRole);
				} else {
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}

		cook.setRoles(roles);
		cookRepository.save(cook);
		return ResponseEntity.ok(new MessageResponse("Cook Registered Successfully"));
	}

	// Cook login (authentication)
	public ResponseEntity<?> authenticateCook(@Valid LoginRequest loginRequest) {
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

	// Get all cooks
	public ResponseEntity<?> getAllCooks() {
		List<Cook> cooks = cookRepository.findAll();
		return ResponseEntity.ok(cooks);
	}

	// Get Cook by ID
	public ResponseEntity<?> getCookById(Long cookid) {
		Optional<Cook> cook = cookRepository.findById(cookid);
		if (cook.isPresent()) {
			return ResponseEntity.ok(cook.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Cook not found"));
		}
	}

	// Update Cook information
	public ResponseEntity<?> updateCook(Long cookId, @Valid CookSignup cookSignup) {
		Optional<Cook> cookOptional = cookRepository.findById(cookId);

		if (!cookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Cook not found"));
		}

		Cook cook = cookOptional.get();
		// Update cook's information
		cook.setUsername(cookSignup.getUsername());
		cook.setEmail(cookSignup.getEmail());
		cook.setPhonenumber(cookSignup.getPhonenumber());
		cook.setCookExperienceLevel(cookSignup.getCookExperienceLevel());
		cook.setCookSpecialty(cookSignup.getCookSpecialty());
		cook.setPassword(encoder.encode(cookSignup.getPassword())); // Update password if needed

		// Handle roles if needed (can be modified as per requirements)
		Set<String> strRoles = cookSignup.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles != null) {
			strRoles.forEach(role -> {
				switch (role) {
				case "cook":
					Role cookRole = roleRepository.findByName(ERole.ROLE_COOK)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(cookRole);
					break;

				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}
		cook.setRoles(roles);

		cookRepository.save(cook);
		return ResponseEntity.ok(new MessageResponse("Cook information updated successfully"));
	}

	// Delete Cook
	public ResponseEntity<?> deleteCook(Long cookid) {
		Optional<Cook> cookOptional = cookRepository.findById(cookid);
		if (!cookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Cook not found"));
		}

		cookRepository.deleteById(cookid);
		return ResponseEntity.ok(new MessageResponse("Cook deleted successfully"));
	}
}
