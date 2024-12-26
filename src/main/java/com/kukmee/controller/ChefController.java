package com.kukmee.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.Mailsender.EmailService;
import com.kukmee.Mailsender.EmailVerificationToken;
import com.kukmee.Mailsender.EmailVerificationTokenRepository;
import com.kukmee.entity.Chef;
import com.kukmee.repository.ChefRepository;
import com.kukmee.request.ChefSignup;
import com.kukmee.request.LoginRequest;
import com.kukmee.response.MessageResponse;
import com.kukmee.service.ChefService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/auth")
public class ChefController {

	@Autowired
	private ChefService chefService;
	

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private EmailService emailService;

	@Autowired
	private ChefRepository chefRepository;

	@PostMapping("/chefsignup")
	public ResponseEntity<?> registerhef(@Valid @RequestBody ChefSignup chefSignup) {
		return chefService.registerChef(chefSignup);
	}

	@PostMapping("/chefsignin")
	public ResponseEntity<?> authenticateChef(@Valid @RequestBody LoginRequest loginRequest) {
		return chefService.authenticateChef(loginRequest);
	}

	@GetMapping("/chefall")
	public ResponseEntity<?> getAllBartenders() {
		return chefService.getAllChefs();
	}

	@GetMapping("/chef/{chefid}")
	public ResponseEntity<?> getBartenderById(@PathVariable Long chefid) {
		return chefService.getChefById(chefid);
	}

	@PutMapping("/chef/{chefid}")
	public ResponseEntity<?> updateBartender(@PathVariable Long chefid, @Valid @RequestBody ChefSignup chefSignup) {
		return chefService.updateChef(chefid, chefSignup);
	}

	@DeleteMapping("/chef/{chefid}")
	public ResponseEntity<?> deleteBartender(@PathVariable Long chefid) {
		return chefService.deleteChef(chefid);
	}
	

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
    	
        Optional<EmailVerificationToken> optionalToken = emailVerificationTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid or expired token"));
        }

        EmailVerificationToken verificationToken = optionalToken.get();
        Chef chef = verificationToken.getChef();

        chef.setEmailVerified(true); 
        chefRepository.save(chef);

        emailVerificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
    }
}
