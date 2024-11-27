package com.kukmee.controller;

import com.kukmee.entity.ERole;
import com.kukmee.entity.Role;
import com.kukmee.entity.Warehouse;
import com.kukmee.repository.RoleRepository;
import com.kukmee.repository.WarehouseRepository;
import com.kukmee.request.LoginRequest;
import com.kukmee.request.WarehouseSignUp;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.service.UserDetailsImpl;
import com.kukmee.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class WarehouseController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	WarehouseRepository warehouseRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/warehousesignup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody WarehouseSignUp warehouseSignUp) {

		if (warehouseRepository.existsByUsername(warehouseSignUp.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}

		if (warehouseSignUp.getPhonenumber() == null
				|| String.valueOf(warehouseSignUp.getPhonenumber()).length() != 10) {
			return ResponseEntity.badRequest().body("Phone number must be exactly 10 digit");
		}

		warehouseSignUp.setEmail(warehouseSignUp.getEmail().trim());

		Warehouse warehouse = new Warehouse(warehouseSignUp.getUsername(),
				encoder.encode(warehouseSignUp.getPassword()), warehouseSignUp.getEmail(),
				warehouseSignUp.getWarehousename(), warehouseSignUp.getLocation(), warehouseSignUp.getPhonenumber(),
				warehouseSignUp.getManagerName());

		// Assign roles to the warehouse
		Set<String> strRoles = warehouseSignUp.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);
					break;
				case "subadmin":
					Role modRole = roleRepository.findByName(ERole.ROLE_SUBADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(modRole);
					break;
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

		// Set roles for the warehouse
		warehouse.setRoles(roles);

		// Save the warehouse to the repository
		warehouseRepository.save(warehouse);

		return ResponseEntity.ok(new MessageResponse("Warehouse Registered Successfully"));
	}

	@PostMapping("/warehousesignin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
	}

	@GetMapping("/warehouseuserAll")
	public ResponseEntity<?> getAllUsers() {
		List<Warehouse> users = warehouseRepository.findAll();
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
