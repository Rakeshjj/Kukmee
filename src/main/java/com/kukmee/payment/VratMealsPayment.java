package com.kukmee.payment;

import com.kukmee.subscription.SubscriptionPlan;
import com.kukmee.vratmeals.VratMealSubscription;

import jakarta.persistence.Column;
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
public class VratMealsPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false, unique = true)
	private String sessionId;

	private double amount;
	private String currency;
	private String status;

	@ManyToOne
	@JoinColumn(name = "vratmeal_id", nullable = false)
	private VratMealSubscription vratMealSubscription;
}
