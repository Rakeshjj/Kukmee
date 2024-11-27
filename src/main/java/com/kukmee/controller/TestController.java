package com.kukmee.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
public class TestController {
     
	@GetMapping("/welcome")
	public String get() {
		return "Welcome to spring security World";
	}
	
	@GetMapping("/customer")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('SUBADMIN') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Access";
	}
	
	@GetMapping("/subadmin")
	@PreAuthorize("hasRole('SUBADMIN') or hasRole('ADMIN')")
	public String moderatorAccess() {
		return "Moderator Access";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board";
	}

	
	
}

