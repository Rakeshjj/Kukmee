package com.kukmee.Mailsender;

import java.time.LocalDateTime;

import com.kukmee.entity.Chef;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EmailVerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@OneToOne
	@JoinColumn(name = "chef_id", referencedColumnName = "chefid")
	private Chef chef;

	private LocalDateTime expiryDate;

	// Getters and setters
}
