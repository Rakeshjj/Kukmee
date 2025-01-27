package com.kukmee.subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kukmee.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Meal name cannot be null")
    private String mealName; // Renamed from mealPreferences

    @NotNull(message = "Subscription plan cannot be null")
    private String subscriptionPlan; // New attribute

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity; // New attribute

    @NotNull(message = "Total amount cannot be null")
    private Double totalAmount; // New attribute

    @NotNull(message = "Address cannot be null")
    private String address; // New attribute

    private boolean isExpired = false;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
