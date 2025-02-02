package com.kukmee.cookbookings;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CookServiceBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "cookDuration cannot be null")
	private String cookDuration;

	@NotNull(message = "numberOfMembers cannot be null")
	private int numberOfMembers;

	@NotNull(message = "serviceStartDate cannot be null")
	private String serviceStartDate;

	@NotNull(message = "mealPreferences cannot be null")
	private String mealPreferences;

	@NotNull(message = "location cannot be null")
	private String location;

	@NotNull(message = "fullName cannot be null")
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Name cannot start with a number")
	private String fullName;

	@NotNull(message = "email cannot be null")
	@Email
	private String email;

	@NotNull(message = "phoneNumber cannot be null")
	private String phoneNumber;

	@NotNull(message = "totalAmount cannot be null")
	private double grandTotal;

	private double gst;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private LocalDateTime updatedDate;

	@PrePersist
	protected void onCreate() {
		createdDate = LocalDateTime.now();
		updatedDate = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = LocalDateTime.now();
	}

}
