package com.ahmad.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingTest {
    private Booking booking;

    @BeforeEach
    void setUp() {
        int showNumber = 1;
        String buyerPhone = "123456";
        String[] seats = {"A1", "B2"};

        booking = new Booking(showNumber, buyerPhone, seats);
    }

    @Test
    void testDefaultConstructor() {
        Booking defaultBooking = new Booking();
        assertNotNull(defaultBooking);
    }

    @Test
    void testGetters() {
        String[] seats = {"A1", "B2"};

        assertEquals(1, booking.getShowNumber());
        assertEquals("123456", booking.getBuyerPhone());
        assertArrayEquals(seats, booking.getSeats());
        assertTrue(booking.getTicketNumber() >= 0 && booking.getTicketNumber() < 100000000);
        assertEquals(0, booking.getMinutesSinceBooking());
    }
}
