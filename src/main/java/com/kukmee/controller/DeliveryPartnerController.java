package com.kukmee.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kukmee.entity.DeliveryPartner;
import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.repository.DeliverPartnerRepo;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.DeliverPartnerSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.service.UserDetailsImpl;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class DeliveryPartnerController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	DeliverPartnerRepo deliverPartnerRepo;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/deliveryusersignup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody DeliverPartnerSignup deliverPartnerSignup) {
		if (deliverPartnerRepo.existsByUsername(deliverPartnerSignup.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already Exists"));
		}

		if (deliverPartnerRepo.existsByVehiclenumber(deliverPartnerSignup.getVehiclenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Vehicle number is already in use!"));
		}

		if (deliverPartnerRepo.existsByLicensenumber(deliverPartnerSignup.getLicensenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: License number is already in use!"));
		}

		DeliveryPartner user = new DeliveryPartner(deliverPartnerSignup.getUsername(), deliverPartnerSignup.getEmail(),
				encoder.encode(deliverPartnerSignup.getPassword()), deliverPartnerSignup.getPhonenumber(),
				deliverPartnerSignup.getVehicletype(), deliverPartnerSignup.getVehiclenumber(),
				deliverPartnerSignup.getLicensenumber());

		Set<String> strRoles = deliverPartnerSignup.getRole();
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

		user.setRoles(roles);
		deliverPartnerRepo.save(user);
		return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
	}

	@PostMapping("/deliveryusersignin")
	public ResponseEntity<?> authenticationUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateToken(authentication);

		UserDetailsImpl userDetailserviceImpl = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetailserviceImpl.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity
				.ok(new JwtResponse(jwt, userDetailserviceImpl.getId(), userDetailserviceImpl.getUsername(), roles));
	}

	@GetMapping("/deliveryuserAll")
	public ResponseEntity<?> getAllUsers() {
		List<DeliveryPartner> users = deliverPartnerRepo.findAll();

		List<?> userDetails = users.stream().map(user -> {
			return new Object() {
				public final Long id = user.getId();
				public final String username = user.getUsername();
				public final Set<String> roles = user.getRoles().stream().map(Role::getName).map(Enum::name)
						.collect(Collectors.toSet());
			};
		}).collect(Collectors.toList());
		return ResponseEntity.ok(userDetails);
	}

}
