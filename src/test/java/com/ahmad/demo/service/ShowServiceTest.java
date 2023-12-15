package com.ahmad.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ahmad.demo.model.Show;
import com.ahmad.demo.repository.ShowRepository;

class ShowServiceTest {

    @Mock
    private ShowRepository showRepository;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindShow() {
        int showNumber = 1;
        Show expectedShow = new Show(showNumber, 3, 4, 2);

        when(showRepository.findByShowNumber(showNumber)).thenReturn(expectedShow);

        Show actualShow = showService.findShow(showNumber);

        assertEquals(expectedShow, actualShow, "findShow should return the expected show");
    }

    @Test
    void testAddShow() {
        int showNumber = 2;
        int numRows = 2;
        int seatsPerRow = 3;
        int cancellationWindow = 1;

        showService.addShow(showNumber, numRows, seatsPerRow, cancellationWindow);

        verify(showRepository).save(any(Show.class));
    }

    @Test
    void testUpdateSeats() {
        int showNumber = 3;
        Show existingShow = new Show(showNumber, 2, 2, 1);
        List<String> availableSeats = Arrays.asList("A1", "A2", "B1", "B2");

        when(showRepository.save(existingShow)).thenReturn(existingShow);

        showService.updateSeats(existingShow, availableSeats);

        verify(showRepository).save(existingShow);

        assertEquals(availableSeats, existingShow.getAvailableSeats(), "Available seats should be updated");
    }
}