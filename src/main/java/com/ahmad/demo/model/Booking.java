package com.ahmad.demo.model;

import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column
    private int ticketNumber;

    @Column
    private int showNumber;

    @Column
    private String buyerPhone;

    @Column
    private String seats;

    @Column
    private long bookingTime;

    public Booking() {}

    public Booking(int showNumber, String buyerPhone, String[] seats) {
        this.ticketNumber = generateTicketNumber();
        this.showNumber = showNumber;
        this.buyerPhone = buyerPhone;
        this.seats = String.join(",", seats);
        this.bookingTime = System.currentTimeMillis();
    }

    private int generateTicketNumber() {
        return ThreadLocalRandom.current().nextInt(0, 100000000);
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public int getShowNumber() {
        return showNumber;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public String[] getSeats() {
        return seats.split(",");
    }
    
    public long getMinutesSinceBooking() {
        return (System.currentTimeMillis() - bookingTime) / (60 * 1000);
    }
}
