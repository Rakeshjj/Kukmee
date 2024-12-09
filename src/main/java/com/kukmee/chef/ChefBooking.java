package com.kukmee.chef;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class ChefBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chefBookingId;

	@NotNull(message = "occasion cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Occasion cannot start with a number")
	private String occasion;

	@NotNull(message = "Date cannot be null")
	@Pattern(regexp = "^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$", message = "Date must be in the format DD-MM-YYYY")
	private String date;

	@NotNull(message = "MealType cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "MealType cannot start with a number")
	private String mealType; // New field for meal type: Breakfast, Lunch, or Dinner

	@NotNull(message = "Time cannot be null")
	@Pattern(regexp = "^(0?[1-9]|1[0-2]):([0-5][0-9])\\s([APap][Mm])$", message = "Time must be in 12-hour format (e.g., 6:30 PM).")
	private String timeSlot;

	@Min(value = 1, message = "Number of people must be at least 1.")
	@Max(value = 10, message = "Number of people cannot exceed 10.")
	private int numberOfPeople;

	@Min(value = 1, message = "Number of GasBurners must be at least 1.")
	@Max(value = 10, message = "Number of GasBurners cannot exceed 6.")
	private int gasBurners;
	
	@NotNull(message = "Location cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Location cannot start with a number")
	private String location;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Dish> dishes;

	@Pattern(regexp = "^[^\\d].*", message = "Cuisine cannot start with a number")
	private String cuisine; // e.g., South Indian, North Indian, etc.

	private double basePrice;
	private double dishCharges;
	private double discount;
	private double gst;
	private double totalAmount;
	private double advancePayment;
	private double balanceAmount;

	// Getters and Setters
}
