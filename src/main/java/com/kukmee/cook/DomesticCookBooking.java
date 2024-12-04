package com.kukmee.cook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class DomesticCookBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Date cannot be null")
	@Pattern(regexp = "^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$", message = "Date must be in the format DD-MM-YYYY")
	private String date;

	@Min(value = 1, message = "Number of people must be at least 1.")
	@Max(value = 10, message = "Number of people cannot exceed 10.")
	private int numberOfPeople;

	@NotNull(message = "Cook Arrival Time cannot be null")
	@Pattern(regexp = "^(0?[1-9]|1[0-2]):([0-5][0-9])\\s([APap][Mm])$", message = "Cook Arrival time must be in 12-hour format (e.g., 6:30 PM).")
	private String cookArrivalTime;

	@NotNull(message = "Location cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Location cannot start with a number")
	private String location;

	private double totalAmount;
	private double advancePayment;
	private double balanceAmount;

	// Getters and Setters
}
