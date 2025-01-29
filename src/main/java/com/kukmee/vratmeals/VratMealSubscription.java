package com.kukmee.vratmeals;

import java.time.LocalDate;

import com.kukmee.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
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
public class VratMealSubscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Plan cannot be null")
	private String selectedPlan;

	@Column(nullable = false)
	@NotBlank
	@NotNull(message = "startdate cannot be null")
	private LocalDate startDate;

	@NotNull(message = "Quantity cannot be null")
	private int quantity;

	@NotNull(message = "Meal price cannot be null")
	private double mealPrice;

	@NotNull(message = "totalAmount cannot be null")
	private double totalAmount;

	@NotNull(message = "address cannot be null")
	private String address;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

}
