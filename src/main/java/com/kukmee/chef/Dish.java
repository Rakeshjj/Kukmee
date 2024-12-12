package com.kukmee.chef;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Dish {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category; // Breakfast, Bread/Rice, Dessert, etc.
	private String name;

	@Pattern(regexp = "^(Breakfast|Lunch|Dinner)$", message = "Invalid meal type")
	private String mealType;

	// Getters and Setters

}
