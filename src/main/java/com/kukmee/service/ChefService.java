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
		
		if (chefRepository.existsByUsername(chefSignup.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}

		if (chefRepository.existsByEmail(chefSignup.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
		}

		if (chefRepository.existsByPhonenumber(chefSignup.getPhonenumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use"));
		}

		Chef chef = new Chef(chefSignup.getUsername(), chefSignup.getEmail(), encoder.encode(chefSignup.getPassword()),
				chefSignup.getPhonenumber(), chefSignup.getYearsOfExperience(), chefSignup.getSpecialty());

		Set<String> strRoles = chefSignup.getRole();
		Set<Role> roles = new HashSet<>();

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

		chef.setRoles(roles);
		chefRepository.save(chef);

		String token = UUID.randomUUID().toString();
		EmailVerificationToken verificationToken = new EmailVerificationToken();
		verificationToken.setToken(token);
		verificationToken.setChef(chef);
		verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); 
		emailVerificationTokenRepository.save(verificationToken);

		String verificationUrl = "http://localhost:8082/api/auth/verify-email?token=" + token;
		emailService.sendSimpleMessage(chefSignup.getEmail(), "Email Verification",
				"Click the link to verify your email: " + verificationUrl);

		return ResponseEntity.ok(new MessageResponse("Chef registered successfully. Please verify your email."));
	}

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

		emailVerificationTokenRepository.delete(verificationToken);

		return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
	}

	public ResponseEntity<?> authenticateChef(@Valid LoginRequest loginRequest) {
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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Error: Invalid username or password"));
		}
	}

	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = chefRepository.findAll();
		return ResponseEntity.ok(chefs);
	}

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
		chef.setPassword(encoder.encode(chefSignup.getPassword())); 

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

	public ResponseEntity<?> deleteChef(Long chefid) {
		Optional<Chef> chefOptional = chefRepository.findById(chefid);
		if (!chefOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Bartender not found"));
		}

		chefRepository.deleteById(chefid);
		return ResponseEntity.ok(new MessageResponse("Bartender deleted successfully"));
	}

}
