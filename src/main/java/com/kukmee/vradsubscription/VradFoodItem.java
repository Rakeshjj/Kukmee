package com.kukmee.vradsubscription;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VradFoodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long foodId;

	private String foodName;

	@ManyToOne
	@JoinColumn(name = "plan_id")
	@JsonIgnore
	private VradSubscriptionPlan subscriptionPlan;

}
