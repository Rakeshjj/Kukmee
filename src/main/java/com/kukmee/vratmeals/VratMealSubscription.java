package com.kukmee.vratmeals;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(String selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getMealPrice() {
		return mealPrice;
	}

	public void setMealPrice(double mealPrice) {
		this.mealPrice = mealPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
