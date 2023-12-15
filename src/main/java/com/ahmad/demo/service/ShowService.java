package com.ahmad.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahmad.demo.model.Show;
import com.ahmad.demo.repository.ShowRepository;

@Service
public class ShowService {
    private final ShowRepository showRepository;

    @Autowired
    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Transactional
    public Show findShow(int showNumber) {
        return showRepository.findByShowNumber(showNumber);
    }

    @Transactional
    public void addShow(int showNumber, int numRows, int seatsPerRow, int cancellationWindow) {
        Show show = new Show(showNumber, numRows, seatsPerRow, cancellationWindow);
        showRepository.save(show);
    }

    @Transactional
    public Show updateSeats(Show show, List<String> availableSeats) {
        show.setAvailableSeats(availableSeats);
        return showRepository.save(show);
    }
}