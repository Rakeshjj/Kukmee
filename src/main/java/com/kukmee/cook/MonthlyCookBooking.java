package com.kukmee.cook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MonthlyCookBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceStartDate;
    private int numberOfPeople;
    private String mealType; // Breakfast, Lunch, or Dinner
    private String address; // Full address
    private double totalAmount;
    private double advancePayment;
    private double balanceAmount;
}
