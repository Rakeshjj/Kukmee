package com.kukmee.subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionRequest {

	private Long customerid;
	private String planType;
	private int mealCount;
}
