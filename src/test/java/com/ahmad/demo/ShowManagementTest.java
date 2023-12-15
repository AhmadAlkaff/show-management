package com.ahmad.demo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ahmad.demo.model.Booking;
import com.ahmad.demo.model.Show;
import com.ahmad.demo.service.BookingService;
import com.ahmad.demo.service.ShowService;

class ShowManagementTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private ShowService showService;

    @InjectMocks
    private ShowManagement showManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructor() {
        MockitoAnnotations.openMocks(this);
        ShowManagement showManagement = new ShowManagement(bookingService, showService);
        assertNotNull(showManagement);
    }

    @Test
    void testProcessAdminSetupSuccess() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2 3 4\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(1)).thenReturn(null);
        try {            
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService).findShow(1);
            verify(showService).addShow(1, 2, 3, 4);
            assertTrue(outContent.toString().contains("Setup for show #1 is completed."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupExistingShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2 3 4\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(1)).thenReturn(new Show(1, 2, 2, 1));
        try {            
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService).findShow(1);
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Show #1 already exists. Please use another show number."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidArgumentsExceedRows() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 100 3 4\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Cannot add 100 rows. Maximum rows limit reached 26."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidArgumentsNegativeRows() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 -1 3 4\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Cannot add -1 rows. Minumum number of rows is 1."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidArgumentsExceedSeatsPerRow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2 100 4\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Cannot add 100 seats per row. Maximum seats per row limit reached 10."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidArgumentsNegativeSeatsPerRow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2 -1 4\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Cannot add -1 seats per row. Minumum number of seats per row is 1."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidArgumentsNegativeCancellationWindow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2 3 -1\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Minimum time for cancellation window is 1."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidParameters() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup A B C D\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Invalid input for parameters. Please provide valid numbers."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminSetupInvalidNumberOfArguments() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Setup 1 2\nBack\n".getBytes());
        System.setIn(in);

        try {                        
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService, never()).findShow(anyInt());
            verify(showService, never()).addShow(anyInt(), anyInt(), anyInt(), anyInt());
            assertTrue(outContent.toString().contains("Invalid number of arguments for 'Setup'. Expected: 'Setup <Show Number> <Number of Rows> <Number of Seats per Row> <Cancellation Window>'."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminViewExistingShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("View 1\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(1)).thenReturn(new Show(1, 2, 2, 1));
        when(bookingService.findAllBookingByShowNumber(1)).thenReturn(Arrays.asList(new Booking(1, "123456", new String[]{"A1", "B2"})));
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService).findShow(1);
            verify(bookingService).findAllBookingByShowNumber(1);
            assertTrue(outContent.toString().contains("Viewing bookings for show #1."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminViewNonExistingShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("View 2\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(1)).thenReturn(new Show(1, 2, 2, 1));
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            verify(showService).findShow(2);
            assertTrue(outContent.toString().contains("Show #2 does not exist."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminViewInvalidShowNumber() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("View ABC\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid input for <Show Number>. Please provide valid <Show Number>."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminViewInvalidParameters() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("View\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid number of arguments for 'View'. Expected: 'View <Show Number>'."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessAdminInvalidCommand() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Done\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processAdmin(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Please enter a valid admin command."));
        } finally {
            System.setIn(sysInBackup);
            System.setOut(System.out);
        }
    }

    @Test
    void testProcessBuyerAvailabilityExistingShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Availability 1\nDone\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(anyInt())).thenReturn(new Show(1, 2, 2, 1));
        try {
            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(showService).findShow(1);
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerAvailabilityNonExistentShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Availability 2\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(anyInt())).thenReturn(null);
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(showService).findShow(2);
            assertTrue(outContent.toString().contains("Show #2 does not exist."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerAvailabilityInvalidShowNumber() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Availability ABC\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid input for <Show Number>. Please provide valid <Show Number>."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerAvailabilityInvalidParameters() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Availability 1 A1,A2\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid number of arguments for 'Availability'. Expected: 'Availability <Show Number>'."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookExistentShowAndSeat() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 1 123456 A1,B2\nBack\n".getBytes());
        System.setIn(in);

        when(bookingService.findBooking(anyInt(), anyString())).thenReturn(null);
        when(showService.findShow(anyInt())).thenReturn(new Show(1, 2, 2, 1));
        when(showService.updateSeats(any(), any())).thenReturn(new Show());

        try {
            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService).addBooking(1, "123456", new String[]{"A1", "B2"});
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookNonExistentShow() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 2 123456 A1,B2\nBack\n".getBytes());
        System.setIn(in);

        when(showService.findShow(anyInt())).thenReturn(null);
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).addBooking(anyInt(), anyString(), any());
            verify(showService, never()).updateSeats(any(), any());
            assertTrue(outContent.toString().contains("Show #2 does not exist."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookExistingReservation() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 1 123456 A1,B2\nBack\n".getBytes());
        System.setIn(in);

        when(bookingService.findBooking(anyString())).thenReturn(new Booking(1, "123456", new String[]{"C1", "D2"}));
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).addBooking(anyInt(), anyString(), any());
            verify(showService, never()).updateSeats(any(), any());
            assertTrue(outContent.toString().contains("Only one booking is allowed per buyer phone number."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookInvalidSeats() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 1 123456 A1+A2\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).addBooking(anyInt(), anyString(), any());
            verify(showService, never()).updateSeats(any(Show.class), any());
            assertTrue(outContent.toString().contains("Invalid format for 'Book' command. Seats should be a comma-separated list (e.g., A1,B2,C3)."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookInvalidShowNumber() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book ABC 123456 A1,B2\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).addBooking(anyInt(), anyString(), any());
            verify(showService, never()).updateSeats(any(), any());
            assertTrue(outContent.toString().contains("Invalid input for <Show Number>. Please provide valid <Show Number>."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookInvalidSeatFormat() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 1 123456 123\nBack\n".getBytes());
        System.setIn(in);

        try {
            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).addBooking(anyInt(), anyString(), any());
            verify(showService, never()).updateSeats(any(), any());
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerBookInvalidParameters() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Book 1\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid number of arguments for 'Book'. Expected: 'Book <Show Number> <Phone#> <Comma separated list of seats>'."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerCancelTicketFound() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Cancel 1 123456\nBack\n".getBytes());
        System.setIn(in);

        when(bookingService.findBooking(1, "123456")).thenReturn(new Booking(1, "123456", new String[]{"A1", "B2"}));
        when(showService.findShow(anyInt())).thenReturn(new Show(1, 2, 2, 1));
        when(showService.updateSeats(any(), any())).thenReturn(new Show());
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService).deleteBooking(1);
            verify(showService).updateSeats(any(Show.class), any());
            assertTrue(outContent.toString().contains("Ticket #1 has been successfully cancelled."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerCancelTicketNotFound() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Cancel 2 654321\nBack\n".getBytes());
        System.setIn(in);

        when(bookingService.findBooking(2, "654321")).thenReturn(null);
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).deleteBooking(anyInt());
            verify(showService, never()).updateSeats(any(Show.class), any());
            assertTrue(outContent.toString().contains("Booking with ticket #2 and buyer phone 654321 does not exist."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerCancelWindowExceeded() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Cancel 1 123456\nBack\n".getBytes());
        System.setIn(in);

        Booking booking = new Booking(1, "123456", new String[]{"A1", "B2"});
        Show show = new Show(1, 2, 2, -1);
        when(bookingService.findBooking(1, "123456")).thenReturn(booking);
        when(showService.findShow(booking.getShowNumber())).thenReturn(show);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).deleteBooking(anyInt());
            verify(showService, never()).updateSeats(any(Show.class), any());
            assertTrue(outContent.toString().contains("Ticket cannot be cancelled as the cancellation window for ticket #1 has been exceeded."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerCancelInvalidTicketNumber() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Cancel ABC 123456\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            verify(bookingService, never()).deleteBooking(anyInt());
            verify(showService, never()).updateSeats(any(Show.class), any());
            assertTrue(outContent.toString().contains("Invalid input for <Ticket#>. Please provide valid <Ticket#>."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerCancelInvalidParameters() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Cancel 1\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Invalid number of arguments for 'Cancel'. Expected: 'Cancel <Ticket#> <Phone#>'."));
        } finally {
            System.setIn(sysInBackup);
        }
    }

    @Test
    void testProcessBuyerInvalidCommand() {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Done\nBack\n".getBytes());
        System.setIn(in);

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            showManagement.processBuyer(new java.util.Scanner(System.in));
            assertTrue(outContent.toString().contains("Please enter a valid buyer command."));
        } finally {
            System.setIn(sysInBackup);
            System.setOut(System.out);
        }
    }

    @Test
    void testValidateNumberOfRow() {
        assertDoesNotThrow(() -> showManagement.validateNumberOfRow(3));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> showManagement.validateNumberOfRow(0));
        assertEquals("Cannot add 0 rows. Minumum number of rows is 1.", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> showManagement.validateNumberOfRow(27));
        assertEquals("Cannot add 27 rows. Maximum rows limit reached 26.", exception.getMessage());
    }

    @Test
    void testValidateSeatsPerRow() {
        assertDoesNotThrow(() -> showManagement.validateSeatsPerRow(3));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> showManagement.validateSeatsPerRow(0));
        assertEquals("Cannot add 0 seats per row. Minumum number of seats per row is 1.", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> showManagement.validateSeatsPerRow(11));
        assertEquals("Cannot add 11 seats per row. Maximum seats per row limit reached 10.", exception.getMessage());
    }

    @Test
    void testValidateCancellationWindow() {
        assertDoesNotThrow(() -> showManagement.validateCancellationWindow(2));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> showManagement.validateCancellationWindow(0));
        assertEquals("Minimum time for cancellation window is 1.", exception.getMessage());
    }
}
