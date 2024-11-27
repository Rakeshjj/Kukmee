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

import com.kukmee.entity.ERole;
import com.kukmee.entity.Hub;
import com.kukmee.entity.Role;
import com.kukmee.repository.HubRepository;
import com.kukmee.repository.RoleRepository;
import com.kukmee.request.HubSignUp;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.JwtResponse;
import com.kukmee.response.MessageResponse;
import com.kukmee.service.UserDetailsImpl;
import com.kukmee.utils.JwtUtils;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class HubController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	HubRepository hubRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/hubusersignup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody HubSignUp hubSignUp) {

		if (hubRepository.existsByUsername(hubSignUp.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already Exists"));
		}

		if (hubSignUp.getPhonenumber() == null || String.valueOf(hubSignUp.getPhonenumber()).length() != 10) {
			return ResponseEntity.badRequest().body("Phone number must be exactly 10 digits");
		}

		hubSignUp.setEmail(hubSignUp.getEmail().trim());

		// Create a new Hub entity and assign roles
		Hub hub = new Hub(hubSignUp.getUsername(), hubSignUp.getPassword(), hubSignUp.getEmail(),
				hubSignUp.getPhonenumber(), hubSignUp.getHubName(), hubSignUp.getLocation(),
				hubSignUp.getManagerName());

		Set<String> strRoles = hubSignUp.getRole();
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
					Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(customerRole);
					break;

				default:
					Role defaultRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(defaultRole);
				}
			});
		}

		hub.setRoles(roles);
		hubRepository.save(hub);
		return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
	}

	@PostMapping("/hubusersignin")
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

	@GetMapping("/hubuserAll")
	public ResponseEntity<?> getAllUsers() {
		List<Hub> users = hubRepository.findAll();

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
