package com.kukmee.bookingrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kukmee.bookingentity.AmountDetails;

public interface AmountDetailsRepository extends JpaRepository<AmountDetails, Long> {
}
