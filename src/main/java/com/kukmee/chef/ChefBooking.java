package com.kukmee.chef;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private Long id;

	private String occasion;
	private String date;
	private String mealType; // New field for meal type: Breakfast, Lunch, or Dinner
	private String timeSlot;
	private int numberOfPeople;
	private int gasBurners;
	private String location;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Dish> dishes;

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
