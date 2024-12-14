package com.kukmee.subscription;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

@Service
public class SubscriptionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private static final double PRICE_PER_MEAL = 30.00; // Price per meal (30 Rupees)

    public Subscription createSubscription(Long customerid, String planType, int mealCount) {
        // Fetch the customer from the database
        Customer user = customerRepository.findById(customerid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the start date to today
        LocalDate startDate = LocalDate.now();

        // Calculate the end date based on the plan type (e.g., monthly or weekly)
        LocalDate endDate;
        if ("monthly".equalsIgnoreCase(planType)) {
            endDate = startDate.plusMonths(1);
        } else if ("weekly".equalsIgnoreCase(planType)) {
            endDate = startDate.plusWeeks(1);
        } else {
            throw new IllegalArgumentException("Invalid plan type");
        }

        // Calculate the price based on the meal count
        double price = calculatePrice(mealCount);

        // Create and populate the subscription object
        Subscription subscription = new Subscription();
        subscription.setCustomer(user);
        subscription.setPlanType(planType);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setPrice(price); // Dynamic price calculation
        subscription.setMealCount(mealCount); // Use the provided mealCount
        subscription.setStatus("ACTIVE");

        // Save the subscription to the database
        return subscriptionRepository.save(subscription);
    }

    private double calculatePrice(int mealCount) {
        // Calculate price based on mealCount
        return mealCount * PRICE_PER_MEAL;
    }
}
