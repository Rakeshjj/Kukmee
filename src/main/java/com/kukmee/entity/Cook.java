package com.kukmee.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cookid;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(nullable = false)
	private String email;

	@Size(max = 120)
	@NotBlank
	@Column(nullable = false)
	private String password;

	private Long phonenumber;
//	private String profilepicture;

	@NotNull(message = "Years of experience cannot be null")
	@Min(value = 1, message = "Years of experience must be at least 1")
	@Max(value = 50, message = "Years of experience cannot exceed 50")
	private Integer cookExperienceLevel; // e.g., Entry-level, Intermediate, Expert

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters")
	private String cookSpecialty; // E.g., Grilling, Baking

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "cook_roles", joinColumns = { @JoinColumn(name = "cook_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public Cook(String username, String email, String password, Long phonenumber, Integer cookExperienceLevel,
			String cookSpecialty) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
		this.cookExperienceLevel = cookExperienceLevel;
		this.cookSpecialty = cookSpecialty;
	}

	// Getters and setters
}
