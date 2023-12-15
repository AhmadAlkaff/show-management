package com.ahmad.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ahmad.demo.model.Booking;
import com.ahmad.demo.model.Show;
import com.ahmad.demo.service.BookingService;
import com.ahmad.demo.service.ShowService;

@SpringBootApplication
public class ShowManagement implements CommandLineRunner {
	private BookingService bookingService;
	private ShowService showService;
	private static final String ADMIN = "ADMIN";
	private static final String AVAILABILITY = "AVAILABILITY";
	private static final String BACK = "BACK";
	private static final String BOOK = "BOOK";
	private static final String BUYER = "BUYER";
	private static final String CANCEL = "CANCEL";
	private static final String SETUP = "SETUP";
	private static final String VIEW = "VIEW";
	private static final String QUIT = "QUIT";
	private static final int MAX_ROWS = 26;
    private static final int MAX_SEATS = 10;
	private static Logger log = LoggerFactory.getLogger(ShowManagement.class);

	@Autowired
	public ShowManagement(BookingService bookingService, ShowService showService) {
		this.bookingService = bookingService;
		this.showService = showService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShowManagement.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		boolean cont = true;
		try (Scanner sc = new Scanner(System.in)) {
			while (cont) {
				log.info("Enter either 'Admin' or 'Buyer' to proceed or 'Quit' to terminate.");
				String action = sc.nextLine();
				switch (action.trim().toUpperCase()) {
					case ADMIN:
						processAdmin(sc);
						break;
					case BUYER:
						processBuyer(sc);
						break;
					case QUIT:
						cont = false;
						break;
					default:
						log.warn("Please enter a valid user selection.");
						break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void processAdmin(Scanner sc) {
		boolean cont = true;
		while (cont) {
			log.info("Enter either 'Setup' or 'View' Command to proceed or 'Back' to go back.");
			String input = sc.nextLine();
			input = input.trim().toUpperCase();
			String[] parts = input.split("\\s+");
			if (parts[0].equals(BACK)) {
				cont = false;
			} else if (parts[0].equals(SETUP)) {
				setupShow(parts);
			} else if (parts[0].equals(VIEW)) {
				viewShow(parts);
			} else {
				log.warn("Please enter a valid admin command.");
			}
		}
	}

	public void processBuyer(Scanner sc) {
		boolean cont = true;
		while (cont) {
			log.info("Enter either 'Availability' or 'Book' or 'Cancel' Command to proceed or 'Back' to go back");
			String input = sc.nextLine();
			input = input.trim().toUpperCase();
			String[] parts = input.split("\\s+");
			if (parts[0].equals(BACK)) {
				cont = false;
			} else if (parts[0].equals(AVAILABILITY)) {
				checkAvailability(parts);
			} else if (parts[0].equals(BOOK)) {
				bookTickets(parts);
			} else if (parts[0].equals(CANCEL)) {
				cancelTickets(parts);
			} else {
				log.warn("Please enter a valid buyer command.");
			}
		}
	}

	public void setupShow(String[] parts) {
		if (parts.length == 5) {
			try {
				int showNumber = Integer.parseInt(parts[1]);
				int numRows = Integer.parseInt(parts[2]);
				int seatsPerRow = Integer.parseInt(parts[3]);
				int cancellationWindow = Integer.parseInt(parts[4]);

				validateNumberOfRow(numRows);
				validateSeatsPerRow(seatsPerRow);
				validateCancellationWindow(cancellationWindow);

				Show show = showService.findShow(showNumber);
				if (show != null) {
					log.warn("Show #{} already exists. Please use another show number.", showNumber);
				} else {
					showService.addShow(showNumber, numRows, seatsPerRow, cancellationWindow);
					log.info("Setup for show #{} is completed.", showNumber);
				}				
			} catch (NumberFormatException e) {
				log.warn("Invalid input for parameters. Please provide valid numbers.");
			} catch (IllegalArgumentException e) {
				log.warn(e.getMessage());
			}
		} else {
			log.warn("Invalid number of arguments for 'Setup'. Expected: 'Setup <Show Number> <Number of Rows> <Number of Seats per Row> <Cancellation Window>'.");
		}
	}

	public void validateNumberOfRow(int numRows) {
		if (numRows < 1) {
			throw new IllegalArgumentException("Cannot add " + numRows + " rows. Minumum number of rows is 1.");
		} else if (numRows > MAX_SEATS) {
			throw new IllegalArgumentException("Cannot add " + numRows + " rows. Maximum rows limit reached " + MAX_ROWS + ".");
		}
	}

	public void validateSeatsPerRow(int seatsPerRow) {
		if (seatsPerRow < 1) {
			throw new IllegalArgumentException("Cannot add " + seatsPerRow + " seats per row. Minumum number of seats per row is 1.");
		} else if (seatsPerRow > MAX_SEATS) {
			throw new IllegalArgumentException("Cannot add " + seatsPerRow + " seats per row. Maximum seats per row limit reached " + MAX_SEATS + ".");
		}
	}

	public void validateCancellationWindow(int cancellationWindow) {
		if (cancellationWindow < 1) {
			throw new IllegalArgumentException("Minimum time for cancellation window is 1.");
		}
	}

	public void viewShow(String[] parts) {
		if (parts.length == 2) {
			try {
				int showNumber = Integer.parseInt(parts[1]);
				log.info("Viewing bookings for show #{}.", showNumber);
				Show show = showService.findShow(showNumber);
				if (show == null) {
					log.warn("Show #{} does not exist.", showNumber);
					return;
				}
				
				log.info("Finding all bookings for show #{}.", showNumber);
				List<Booking> bookings = bookingService.findAllBookingByShowNumber(showNumber);
				for (Booking booking: bookings) {
					log.info("Show Number: {}, Ticket #: {}, Buyer Phone #: {}, Seat Numbers: {}.", showNumber, booking.getTicketNumber(), booking.getBuyerPhone(), booking.getSeats());
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid input for <Show Number>. Please provide valid <Show Number>.");
			}
		} else {
			log.warn("Invalid number of arguments for 'View'. Expected: 'View <Show Number>'.");
		}
	}

	public void checkAvailability(String[] parts) {
		if (parts.length == 2) {
			try {
				int showNumber = Integer.parseInt(parts[1]);
				Show show = showService.findShow(showNumber);
				if (show == null) {
					log.warn("Show #{} does not exist.", showNumber);
				} else {
					show.displayAvailableSeats();
				}
			} catch (NumberFormatException e) {
				log.error("Invalid input for <Show Number>. Please provide valid <Show Number>.");
			}
		} else {
			log.error("Invalid number of arguments for 'Availability'. Expected: 'Availability <Show Number>'.");
		}
	}

	public void bookTickets(String[] parts) {
		if (parts.length == 4) {
			try {
				int showNumber = Integer.parseInt(parts[1]);
				String buyerPhone = parts[2];
				String[] seatsToBook = parts[3].split(",");

				for (String seat : seatsToBook) {
					if (!seat.matches("^[A-Za-z0-9]+$")) {
						log.error("Invalid format for 'Book' command. Seats should be a comma-separated list (e.g., A1,B2,C3).");
						return;
					}
				}
				Booking existingBooking = bookingService.findBooking(buyerPhone);
				if (existingBooking != null) {
					log.warn("Only one booking is allowed per buyer phone number.");
					return;
				}
				Show show = showService.findShow(showNumber);
				if (show == null) {
					log.warn("Show #{} does not exist.", showNumber);
				} else if (show.checkAvailableSeats(seatsToBook)) {
					String seats = Arrays.toString(seatsToBook);
					log.info("Booking tickets for show #{} with buyer phone: {} for seats: {}.", show.getShowNumber(), buyerPhone, seats);
					int ticketNumber = bookingService.addBooking(showNumber, buyerPhone, seatsToBook);
					show.removeSeats(seatsToBook);
					showService.updateSeats(show, show.getAvailableSeats());
					log.info("Booking for show #{} has been succesful. Ticket #{} is generated.", showNumber, ticketNumber);
				}
			} catch (NumberFormatException e) {
				log.error("Invalid input for <Show Number>. Please provide valid <Show Number>.");
			}
		} else {
			log.error("Invalid number of arguments for 'Book'. Expected: 'Book <Show Number> <Phone#> <Comma separated list of seats>'.");
		}
	}

	public void cancelTickets(String[] parts) {
		if (parts.length == 3) {
			try {
				int ticketNumber = Integer.parseInt(parts[1]);
				String buyerPhone = parts[2];
				Booking booking = bookingService.findBooking(ticketNumber, buyerPhone);
				if (booking == null) {
					log.warn("Booking with ticket #{} and buyer phone {} does not exist.", ticketNumber, buyerPhone);
				} else {
					log.info("Cancelling booking with ticket #{} for show #{}.", ticketNumber, booking.getShowNumber());
					Show show = showService.findShow(booking.getShowNumber());
					log.info("Ticket #{} was Booked {} Minute(s) Ago", booking.getTicketNumber(), booking.getMinutesSinceBooking());
					if (booking.getMinutesSinceBooking() <= show.getCancellationWindow()) {
						show.addSeats(booking.getSeats());
						bookingService.deleteBooking(ticketNumber);
						showService.updateSeats(show, show.getAvailableSeats());
						log.info("Ticket #{} has been successfully cancelled.", ticketNumber);
					} else {
						log.info("Ticket cannot be cancelled as the cancellation window for ticket #{} has been exceeded.", ticketNumber);
					}
				}
			} catch (NumberFormatException e) {
				log.error("Invalid input for <Ticket#>. Please provide valid <Ticket#>.");
			}
		} else {
			log.error("Invalid number of arguments for 'Cancel'. Expected: 'Cancel <Ticket#> <Phone#>'.");
		}
	}
}
