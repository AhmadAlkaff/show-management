package com.ahmad.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowTest {
    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show(1, 2, 3, 2);
    }

    @Test
    void testDefaultConstructor() {
        Show defaultShow = new Show();
        assertNotNull(defaultShow);
    }

    @Test
    void testGetters() {
        assertEquals(1, show.getShowNumber());
        assertEquals(2, show.getNumRows());
        assertEquals(3, show.getSeatsPerRow());
        assertEquals(2, show.getCancellationWindow());
        assertNotNull(show.getAvailableSeats());
    }

    @Test
    void testSetAvailableSeats() {
        List<String> newAvailableSeats = Arrays.asList("A1", "A2", "B1");
        show.setAvailableSeats(newAvailableSeats);
        assertEquals(newAvailableSeats, show.getAvailableSeats());
    }

    @Test
    void testInitializeAvailableSeats() {
        List<String> expectedSeats = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3");
        assertEquals(expectedSeats, show.getAvailableSeats(), "Available seats should be initialized correctly");
    }

    @Test
    void testGenerateSeatLabel() {
        assertEquals("A1", show.generateSeatLabel(1, 1));
        assertEquals("B2", show.generateSeatLabel(2, 2));
        assertEquals("C10", show.generateSeatLabel(3, 10));
    }

    @Test
    void testCheckAvailableSeats() {
        Show show = new Show(2, 2, 2, 1);
        show.setAvailableSeats(Arrays.asList("A1", "A2", "B1", "B2"));

        String[] seatsToBook = { "A1", "A2", "B1" };
        assertTrue(show.checkAvailableSeats(seatsToBook), "All seats should be available");

        String[] seatsToBookInvalid = { "A1", "A2", "C1" };
        assertFalse(show.checkAvailableSeats(seatsToBookInvalid), "One seat is invalid, should return false");
    }

    @Test
    void testRemoveSeats() {
        show.setAvailableSeats(new ArrayList<>(Arrays.asList("A1", "A2", "B1", "B2")));

        String[] seatsToRemove = { "A1", "B2" };
        show.removeSeats(seatsToRemove);

        List<String> expectedSeats = Arrays.asList("A2", "B1");
        assertEquals(expectedSeats, show.getAvailableSeats(), "Seats should be removed");
    }

    @Test
    void testAddSeats() {
        show.setAvailableSeats(new ArrayList<>(Arrays.asList("A2", "B1")));

        String[] seatsToAdd = { "A1", "B2" };
        show.addSeats(seatsToAdd);

        List<String> expectedSeats = Arrays.asList("A1", "A2", "B1", "B2");
        assertEquals(expectedSeats, show.getAvailableSeats(), "Seats should be added and sorted");
    }
}
