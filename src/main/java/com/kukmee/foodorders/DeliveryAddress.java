package com.kukmee.foodorders;

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

    private String street;
    private String city;
    private String state;
    private String zipCode;
	@Override
	public String toString() {
		return "DeliveryAddress [street=" + street + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode
				+ "]";
	}
    
    
}
