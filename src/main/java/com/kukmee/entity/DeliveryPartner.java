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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DeliveryPartner", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "phonenumber"), @UniqueConstraint(columnNames = "email") })
public class DeliveryPartner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Username cannot start with a number")
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

	private String phonenumber;

	@NotNull(message = "Vehicle type is required")
	private String vehicletype;

	@NotNull(message = "Vehicle number is required")
	@Pattern(regexp = "^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{4}$", message = "Invalid vehicle number format (e.g., KA 01 AB 1234)")
	@Column(nullable = false, unique = true)
	private String vehiclenumber;

	@NotNull(message = "License number is required")
	@Size(min = 10, max = 15, message = "License number must be between 10 and 15 characters")
	@Column(nullable = false, unique = true)
	private String licensenumber;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "deliverypartner_roles", joinColumns = {
			@JoinColumn(name = "deliverpartner_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	private boolean isActive = true;

	public DeliveryPartner(String username, String email, String password, String phonenumber, String vehicletype,
			String vehiclenumber, String licensenumber) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
		this.vehicletype = vehicletype;
		this.vehiclenumber = vehiclenumber;
		this.licensenumber = licensenumber;
	}

}
