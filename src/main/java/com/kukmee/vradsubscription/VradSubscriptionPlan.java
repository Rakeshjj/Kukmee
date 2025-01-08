package com.kukmee.vradsubscription;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class VradSubscriptionPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long planId;

	private String planType;
	private int duration; // Duration in days
	private double cost;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subscriptionPlan")
	@JsonManagedReference
	private List<VradFoodItem> foodItems;

}
