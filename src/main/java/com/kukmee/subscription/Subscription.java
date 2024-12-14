package com.kukmee.subscription;

import java.time.LocalDate;

import com.kukmee.entity.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private Customer customer;

	private String planType; // E.g., weekly, monthly
	private double price;
	private int mealCount; // Number of meals included in the plan
	private LocalDate startDate; // The start date of the subscription
	private LocalDate endDate; // The end date of the subscription

	private String status; // ACTIVE, EXPIRED, CANCELLED
}
