# Show Management

## Description

The Show Management application is a Java-based system designed to facilitate the management of shows, bookings, and seat availability. It offers administrative and buyer functionalities for creating shows, viewing bookings, checking seat availability, booking tickets, and canceling bookings.

### Key Features

#### Admin Functionality

Set up new shows with configurable parameters such as the number of rows, seats per row, and cancellation window.
View bookings for a specific show.

#### Buyer Functionality

Check seat availability for a specific show.
Book tickets for available seats.
Cancel booked tickets within the specified cancellation window.

### How to Use

#### Setup a Show

As an admin, use the 'Admin' option and choose 'Setup' to create a new show by specifying show parameters.

- **Command:** `Setup <Show Number> <Number of Rows> <Number of Seats per Row> <Cancellation Window>`
- **Example:** `Setup 101 10 15 30`
- **Description:** Set up a new show with Show Number 101, 10 rows, 15 seats per row, and a cancellation window of 30 minutes.

### View Bookings

Admins can view all bookings for a specific show using the 'Admin' option and choosing 'View.'

- **Command:** `View <Show Number>`
- **Example:** `View 101`
- **Description:** View bookings for the show with Show Number 101. Display Show Number, Ticket Number, Buyer Phone Number, and Seat Numbers allocated to the buyer.

### Check Seat Availability

Buyers can check seat availability for a show by selecting 'Buyer' and choosing 'Availability.'

- **Command:** `Availability <Show Number>`
- **Example:** `Availability 101`
- **Description:** List all available seat numbers for the show with Show Number 101. (e.g., A1, F4, etc.)

### Book Tickets

Buyers can book tickets for available seats by selecting 'Buyer' and choosing 'Book.'

- **Command:** `Book <Show Number> <Phone#> <Comma separated list of seats>`
- **Example:** `Book 101 1234567890 A1,B2,C3`
- **Description:** Book tickets for the show with Show Number 101, using the provided buyer phone number (1234567890) for the specified seats (A1, B2, C3).

### Cancel Tickets

Buyers can cancel booked tickets within the cancellation window by selecting 'Buyer' and choosing 'Cancel.'

- **Command:** `Cancel <Ticket#> <Phone#>`
- **Example:** `Cancel 12345 1234567890`
- **Description:** Cancel the ticket with Ticket Number 12345 for the buyer with Phone Number 1234567890. Constraints apply (see below).

### Constraints

#### Maximum Seats Per Row

Maximum seats per row is 10.
Example seat numbers are A1, H5, etc.

#### Maximum Number of Rows

Maximum number of rows is 26.

#### Cancellation Window

After booking, users can cancel seats within a time window of 2 minutes (configurable).
Cancellation after that is not allowed.

#### Booking Limitation

Only one booking per phone number is allowed per show.

## Getting Started

Download the project and set it up in your preferred IDE. Ensure that the Java Compiler used is version 1.8.

### Prerequisites

Java 8
Maven 3.6+

### Installing

Make sure the environment variable path for Java has been set up.

## Executing Test Cases

To run the test cases, use the following command:

- mvn clean test

## Execute Test Cases with Report

To run the test cases and generate a report, use the following command:

- mvn clean package verify surefire-report:report

The report will be located at [surefire-report.html](target/site/surefire-report.html)

## Execution

To build the project without running tests and generate the executable JAR file, use the following command:

- mvn clean package -DskipTests

The generated executable JAR file, named **show-management.jar**, will be available in the target directory.

To execute the application, use the following command:

- java -jar show-management.jar

This command executes the JAR file, launching the Show Management application.

## Version

1.0 (14/12/2023)

## Authors

- **Ahmad Alkaff**
