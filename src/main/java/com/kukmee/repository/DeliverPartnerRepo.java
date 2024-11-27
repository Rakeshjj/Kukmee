package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.DeliveryPartner;

public interface DeliverPartnerRepo extends JpaRepository<DeliveryPartner, Long> {

    Optional<DeliveryPartner> findByUsername(String username);

    Optional<DeliveryPartner> findByLicensenumber(String licensenumber);

  
    Optional<DeliveryPartner> findByVehiclenumber(String vehiclenumber);

    boolean existsByVehiclenumber(String vehiclenumber); 

    boolean existsByLicensenumber(String licensenumber); 
    
    boolean existsByUsername(String username);
}

