package com.ahmad.demo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ahmad.demo.model.Booking;
import com.ahmad.demo.repository.BookingRepository;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBookingByShowNumber() {
        int showNumber = 1;
        List<Booking> expectedBookings = Arrays.asList(
            new Booking(showNumber, "1234567890", new String[] { "A1", "A2" }),
            new Booking(showNumber, "9876543210", new String[] { "B1", "B2" })
        );

        when(bookingRepository.findAllBookingByShowNumber(showNumber)).thenReturn(expectedBookings);
        List<Booking> actualBookings = bookingService.findAllBookingByShowNumber(showNumber);
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    void testFindBookingByBuyerPhone() {
        String buyerPhone = "1234567890";
        Booking expectedBooking = new Booking(1, buyerPhone, new String[] { "A1", "A2" });

        when(bookingRepository.findByBuyerPhone(buyerPhone)).thenReturn(expectedBooking);
        Booking actualBooking = bookingService.findBooking(buyerPhone);
        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    void testFindBookingByTicketNumberAndBuyerPhone() {
        int ticketNumber = 1;
        String buyerPhone = "1234567890";
        Booking expectedBooking = new Booking(1, buyerPhone, new String[] { "A1", "A2" });

        when(bookingRepository.findByTicketNumberAndBuyerPhone(ticketNumber, buyerPhone)).thenReturn(expectedBooking);
        Booking actualBooking = bookingService.findBooking(ticketNumber, buyerPhone);
        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    void testAddBooking() {
        int showNumber = 1;
        String buyerPhone = "1234567890";
        String[] seats = { "A1", "A2" };

        bookingService.addBooking(showNumber, buyerPhone, seats);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testDeleteBooking() {
        int ticketNumber = 1;

        bookingService.deleteBooking(ticketNumber);
        verify(bookingRepository).deleteByTicketNumber(ticketNumber);
    }
}
