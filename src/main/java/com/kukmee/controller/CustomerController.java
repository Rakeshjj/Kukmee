package com.kukmee.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.entity.Customer;
import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.repository.CustomerRepository;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.CustomerSignUp;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.service.UserDetailsImpl;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class CustomerController {

	AuthenticationManager authenticationManager;

	CustomerRepository customerRepository;

	RoleRepository roleRepository;

	JwtUtils jwtUtils;

	PasswordEncoder encoder;

	public CustomerController(AuthenticationManager authenticationManager, CustomerRepository customerRepository,
			RoleRepository roleRepository, JwtUtils jwtUtils, PasswordEncoder encoder) {
		super();
		this.authenticationManager = authenticationManager;
		this.customerRepository = customerRepository;
		this.roleRepository = roleRepository;
		this.jwtUtils = jwtUtils;
		this.encoder = encoder;
	}

	@PostMapping("/customersignup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody CustomerSignUp signUpResquest) {
		if (customerRepository.existsByUsername(signUpResquest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already Exists"));
		}

		if (signUpResquest.getPhonenumber() == null || String.valueOf(signUpResquest.getPhonenumber()).length() != 10) {
			return ResponseEntity.badRequest().body("Phone number must be exactly 10 digit");
		}

		Customer customer = new Customer(signUpResquest.getUsername(), signUpResquest.getEmail(),
				encoder.encode(signUpResquest.getPassword()), signUpResquest.getPhonenumber());

		Set<String> strRoles = signUpResquest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "customer":
					Role deliveryRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(deliveryRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
		}

		customer.setRoles(roles);
		customerRepository.save(customer);
		return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
	}

	@PostMapping("/customersignin")
	public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));

		} catch (Exception e) {
			return ResponseEntity.status(401).body(new MessageResponse("Error: Invalid username or password"));
		}
	}

	@GetMapping("/customers")
	public ResponseEntity<?> getAllUsers() {
		List<Customer> users = customerRepository.findAll();

		List<?> userDetails = users.stream().map(user -> {
			return new Object() {
				public final Long id = user.getCustomerid();
				public final String username = user.getUsername();
				public final Set<String> roles = user.getRoles().stream().map(Role::getName).map(Enum::name)
						.collect(Collectors.toSet());
			};
		}).collect(Collectors.toList());
		return ResponseEntity.ok(userDetails);
	}
}
