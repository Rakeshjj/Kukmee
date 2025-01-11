package com.kukmee.subscription;

import java.time.LocalDate;
import java.util.List;

import com.kukmee.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class SubscriptionPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "PlanType cannot be null")
	private String planType; // "vrad", "dietary", or "catering"

	private String description; // Brief description of the subscription

	@NotNull(message = "Cost cannot be null")
	private Double cost;

	@NotNull(message = "Duration cannot be null")
	private Integer duration; // Duration in days (e.g., 7, 14, 30)

	@ElementCollection
	private List<String> mealPreferences; // Meals included, e.g., "Fasting Meals", "Dietary Meals"

	@Column(nullable = false)
	private String availability; // "Daily", "Weekly", or "Custom"

	@Column(nullable = false)
	private LocalDate startDate;

	private boolean isExpired = false;
	@ManyToOne
	@JoinColumn(name = "customer_id") // foreign key column
	private Customer customer;
}
