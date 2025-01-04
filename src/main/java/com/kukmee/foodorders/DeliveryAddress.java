package com.kukmee.foodorders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DeliveryAddress {

	@Column(nullable = false)
	private String contactPerson;
	
	@Column(nullable = false)
	private Long phoneNumber;
	
	@Column(nullable = false)
	private String street;
	
	@Column(nullable = false)
	private String landMark;
	
	@Column(nullable = false)
	private String city;

	@Override
	public String toString() {
		return "DeliveryAddress [contactPerson=" + contactPerson + ", phoneNumber=" + phoneNumber + ", street=" + street
				+ ", landMark=" + landMark + ", city=" + city + "]";
	}

}
