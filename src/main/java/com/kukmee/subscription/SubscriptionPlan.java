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
	private String planType; 
	private String description; 
	@NotNull(message = "Cost cannot be null")
	private Double cost;

	@NotNull(message = "Duration cannot be null")
	private Integer duration; 
	@ElementCollection
	private List<String> mealPreferences; 
	@Column(nullable = false)
	private String availability; 
	@Column(nullable = false)
	private LocalDate startDate;

	private boolean isExpired = false;
	@ManyToOne
	@JoinColumn(name = "customer_id") 
	private Customer customer;
}
