package com.ahmad.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahmad.demo.model.Booking;
import com.ahmad.demo.repository.BookingRepository;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public List<Booking> findAllBookingByShowNumber(int showNumber) {
        return bookingRepository.findAllBookingByShowNumber(showNumber);
    }

    @Transactional
    public Booking findBooking(String buyerPhone) {
        return bookingRepository.findByBuyerPhone(buyerPhone);
    }

    @Transactional
    public Booking findBooking(int ticketNumber, String buyerPhone) {
        return bookingRepository.findByTicketNumberAndBuyerPhone(ticketNumber, buyerPhone);
    }

    @Transactional
    public int addBooking(int showNumber, String buyerPhone, String[] seats) {
        Booking booking = new Booking(showNumber, buyerPhone, seats);
        bookingRepository.save(booking);
        return booking.getTicketNumber();
    }

    @Transactional
    public void deleteBooking(int ticketNumber) {
        bookingRepository.deleteByTicketNumber(ticketNumber);
    }
}
