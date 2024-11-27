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
import jakarta.validation.constraints.NotBlank;
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
public class Chef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chefid;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters")
	private String username;

	@Email
	@Size(max = 50)
	private String email;

	@Size(max = 120)
	@NotBlank
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Long phonenumber; //

	private Integer yearsOfExperience;

	private String specialty; // E.g., Italian, French, Vegan

	private boolean emailVerified = false;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "chef_roles", joinColumns = { @JoinColumn(name = "chef_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public Chef(
			@Size(max = 50) @NotBlank @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters") String username,
			@Email @Size(max = 50) String email, @Size(max = 120) @NotBlank String password, Long phonenumber,
			Integer yearsOfExperience, String specialty) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
		this.yearsOfExperience = yearsOfExperience;
		this.specialty = specialty;
	}

}
