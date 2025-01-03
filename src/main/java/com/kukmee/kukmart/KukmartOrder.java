package com.kukmee.kukmart;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KukmartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerUsername;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<KukmartOrderItem> items;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private String status; 

    @Column(nullable = false)
    private String pickupDate;

    // Getters and Setters
}
