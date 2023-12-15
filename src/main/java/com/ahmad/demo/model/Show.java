package com.ahmad.demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Entity
@Table(name = "shows")
public class Show {

    @Id
    @Column
    private int showNumber;

    @Column
    private int numRows = 0;

    @Column
    private int seatsPerRow = 0;

    @Column
    private int cancellationWindow = 2;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "available_seats", joinColumns = @JoinColumn(name = "show_number"))
    @Column(name = "seat")
    private List<String> availableSeats = new ArrayList<>();

    private static Logger log = LoggerFactory.getLogger(Show.class);

    public Show() {}

    public Show(int showNumber, int numRows, int seatsPerRow, int cancellationWindow) {
        this.showNumber = showNumber;
        this.numRows = numRows;
        this.seatsPerRow = seatsPerRow;
        this.cancellationWindow = cancellationWindow;
        initializeAvailableSeats();
    }
    
    public int getShowNumber() {
        return showNumber;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getCancellationWindow() {
        return cancellationWindow;
    }

    public List<String> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<String> availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void initializeAvailableSeats() {
        for (int row = 1; row <= numRows; row++) {
            for (int seat = 1; seat <= seatsPerRow; seat++) {
                String seatLabel = generateSeatLabel(row, seat);
                availableSeats.add(seatLabel);
            }
        }
        String joinedSeats = String.join(", ", availableSeats);
        log.info("Generated seats for show #{}: [{}].", showNumber, joinedSeats);
    }

    public String generateSeatLabel(int row, int seat) {
        return String.valueOf((char) ('A' + row - 1)) + seat;
    }
    
    public void displayAvailableSeats() {
        log.info("Available seats for show #{}: {}.", showNumber, availableSeats);
    }

    public boolean checkAvailableSeats(String[] seatsToBook) {
        for (String seat : seatsToBook) {
            if (!availableSeats.contains(seat)) {
                log.info("Seat {} is unavailable for show #{}.", seat, showNumber);
                return false;
            }
        }
        return true;
    }

    public void removeSeats(String[] seats) {
        log.info("Removing Seats");
        for (String seat : seats) {
            availableSeats.remove(seat);
        }
    }

    public void addSeats(String[] seats) {
        log.info("Adding Seats");
        availableSeats.addAll(Arrays.asList(seats));
        List<String> sortedSeats = new ArrayList<>(availableSeats);
        Collections.sort(sortedSeats);
        availableSeats = sortedSeats;
    }
}