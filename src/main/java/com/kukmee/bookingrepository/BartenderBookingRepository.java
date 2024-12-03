package com.kukmee.bookingrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.bookingentity.BartenderBooking;

public interface BartenderBookingRepository extends JpaRepository<BartenderBooking, Long> {
}

