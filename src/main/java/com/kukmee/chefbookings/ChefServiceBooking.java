package com.kukmee.chefbookings;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChefServiceBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "ChefType cannot be null")
	private String chefType;

	@NotNull(message = "Categories cannot be null")
	private String categories;

	@NotNull(message = "Duration cannot be null")
	private int duration;

	@NotNull(message = "serviceStartDate cannot be null")
	private LocalDate serviceStartDate;

	private LocalDate serviceEndDate;

	@NotNull(message = "EventType cannot be null")
	private String eventType;

	@NotNull(message = "MealPreferences cannot be null")
	private String mealPreferences;

	@NotNull(message = "location cannot be null")
	private String location;

	@NotNull(message = "FullName cannot be null")
	private String fullName;

	@NotNull(message = "Email cannot be null")
	private String email;

	@NotNull(message = "PhoneNumber cannot be null")
	private String phoneNumber;

}
