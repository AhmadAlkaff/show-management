package com.ahmad.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahmad.demo.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllBookingByShowNumber(int showNumber);

    Booking findByBuyerPhone(String buyerPhone);

    Booking findByTicketNumberAndBuyerPhone(int ticketNumber, String buyerPhone);

    void deleteByTicketNumber(int ticketNumber);
}