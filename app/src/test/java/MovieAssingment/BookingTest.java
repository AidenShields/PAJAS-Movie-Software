package MovieAssingment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    static Booking booking = null;
    static  LocalDateTime sessionTime = null;

    @BeforeAll
    static void beforeTests() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        sessionTime = LocalDateTime.parse(LocalDateTime.now().plus(2, ChronoUnit.HOURS).format(dtf), dtf);
        Movie fnf = new Movie("Fast and Furious 18923842 - Family", Movie.Rating.M, "One of the 18 millionth installations of the epic Fast and Furious franchise and this time, family gets personal", null, "James Bond", new ArrayList<String>());
        fnf.addCastMember("Vin Petrol");
        fnf.addCastMember("Vin Solar");
        Cinema cinema = new Cinema(Cinema.CinemaType.Bronze, (float) 420.69, 100, 100, 100, new ArrayList<Session>());


        Session session = new Session(sessionTime, fnf, cinema);

        session.setNumBookings(1);
        session.setAvailableFrontSeats(99);
        cinema.addSession(session);
        Customer customer = new Customer("Archie", "ArchiesSecurePassword");
        booking = new Booking("0002948", session, customer, true, "", Position.Front, LocalDateTime.now());
        booking.setCustomer(customer);
        booking.setBookingStatus(true);
    }

    @Test
    void getBookingID() {
        //Check ID
        System.out.println("Testing the booking getter ID\nFunction call: booking.getBookingID()\nExpected output: 0002948\nOutput: " + booking.getBookingID());
        assertEquals(booking.getBookingID(), "0002948");
    }

    @Test
    void setBookingID() {
        //Check original ID
        assertEquals(booking.getBookingID(), "0002948");
        //Set to 1
        booking.setBookingID("1");
        System.out.println("Testing the booking setID method\nFunction call: booking.setBookingID(\"1\") + booking.getBookingID()\nExpected output: 1\nOutput: " + booking.getBookingID());
        assertEquals(booking.getBookingID(), "1");
        //Revert to original state to not affect other tests
        booking.setBookingID("0002948");
        assertEquals(booking.getBookingID(), "0002948");
    }

    @Test
    void getSession() {
        System.out.println("Checking that the session is correctly stored in the booking by checking a number of fields such as:\n - Session time\n - Number of bookings\n - Title\n - Cinema type\n - Ticket price\nand so on...");
        //Check the session is at correct time
        assertEquals(booking.getSession().getSessionTime(), sessionTime);
        //This should be the only booking of the session and the session should have 99 front seats
        assertEquals(booking.getSession().getNumBookings(), 1);
        assertEquals(booking.getSession().getAvailableFrontSeats(), 99);
        //Check the movie was correctly added (check some random attributes to see if all data stored)
        assertEquals(booking.getSession().getMovie().getTitle(), "Fast and Furious 18923842 - Family");
        assertEquals(booking.getSession().getMovie().getCast().get(0), "Vin Petrol");
        //Check correct cinema (check some random attributes to see if all data stored)
        assertEquals(booking.getSession().getCinema().getCinemaType(), Cinema.CinemaType.Bronze);
        assertEquals(booking.getSession().getCinema().getTicketPrice(), (float) 420.69);
        assertEquals(booking.getSession().getCinema().getNumMiddleSeats(), 100);
    }

    @Test
    void setSession() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        LocalDateTime oldSessionTime = sessionTime;
        Session oldSession = booking.getSession();

        sessionTime = LocalDateTime.parse(LocalDateTime.now().plus(4, ChronoUnit.HOURS).format(dtf), dtf);

        Movie movie = new Movie("The 20 hours of agile software lectures I'm behind", Movie.Rating.R, "Scrum methods. Agile development. Continuos delivery. A true blockbuster.", null, "Baseem", new ArrayList<String>());
        movie.addCastMember("Qifan Chen");
        movie.addCastMember("Pain");
        Cinema cinema = new Cinema(Cinema.CinemaType.Gold, (float) 1500.00, 200, 150, 200, new ArrayList<Session>());
        Session sessionTwo = new Session(sessionTime, movie, cinema);
        sessionTwo.setNumBookings(1);
        sessionTwo.setAvailableFrontSeats(199);
        cinema.addSession(sessionTwo);

        booking.setSession(sessionTwo);

        System.out.println("Checking that the session can be changed and correctly stored through the setSession() method in the booking by checking a number of fields such as:\n - Session time\n - Number of bookings\n - Title\n - Cinema type\n - Ticket price\nand so on...");
        //Check the session is at correct time
        assertEquals(booking.getSession().getSessionTime(), sessionTime);
        //This should be the only booking of the session and the session should have 99 front seats
        assertEquals(booking.getSession().getNumBookings(), 1);
        assertEquals(booking.getSession().getAvailableFrontSeats(), 199);
        //Check the movie was correctly added (check some random attributes to see if all data stored)
        assertEquals(booking.getSession().getMovie().getTitle(), "The 20 hours of agile software lectures I'm behind");
        assertEquals(booking.getSession().getMovie().getCast().get(0), "Qifan Chen");
        //Check correct cinema (check some random attributes to see if all data stored)
        assertEquals(booking.getSession().getCinema().getCinemaType(), Cinema.CinemaType.Gold);
        assertEquals(booking.getSession().getCinema().getTicketPrice(), (float) 1500.00);
        assertEquals(booking.getSession().getCinema().getNumMiddleSeats(), 150);

        sessionTime = oldSessionTime;
        booking.setSession(oldSession);
    }

    @Test
    void getCustomer() {
        System.out.println("Checking that the customer can be stored correctly through in the booking by checking a number of fields such as:\n - Customer name\n - Customer password\n - Card (should be null as not saved)");
        Customer customer = booking.getCustomer();
        assertNotNull(customer);
        assertEquals(customer.getUsername(), "Archie");
        assertEquals(customer.getPassword(), "ArchiesSecurePassword");
        assertNull(customer.getSavedCreditCard());
    }

    @Test
    void setCustomer() {
        System.out.println("Checking that the customer can be updated and stored correctly in the booking class  by checking a number of fields such as:\n - Customer name\n - Customer password\n - Card (should be null as not saved)\nAfter the customer has been set");
        Customer oldCustomer = booking.getCustomer();
        Customer newCustomer = new Customer("Jack", "trashDumbJackPassword");
        booking.setCustomer(newCustomer);

        Customer customer = booking.getCustomer();
        assertNotNull(customer);
        assertEquals(customer.getUsername(), "Jack");
        assertEquals(customer.getPassword(), "trashDumbJackPassword");
        assertNull(customer.getSavedCreditCard());

        booking.setCustomer(oldCustomer);
    }

    @Test
    void isBookingStatus() {
        System.out.println("Testing that booking status is successful\nExpected: true\nOutput: " + booking.isBookingStatus());
        assertEquals(booking.isBookingStatus(), true);
    }

    @Test
    void setBookingStatus() {
        booking.setBookingStatus(false);
        System.out.println("Testing that booking status can be updated\nExpected: false\nOutput: " + booking.isBookingStatus());
        assertEquals(booking.isBookingStatus(), false);
        booking.setBookingStatus(true);
    }

    @Test
    void getReasonFailed() {
        System.out.println("Testing that the booking class has no failure reason\nFunction call: getReasonFailed()\nExpefcted output: \"\"\nOutput: " + booking.getReasonFailed());
        assertEquals(booking.getReasonFailed(), "");
    }

    @Test
    void setReasonFailed() {
        booking.setReasonFailed("Broke ass");
        System.out.println("Testing that the booking class can have failure reason set\nFunction call: getReasonFailed()\nExpected output: \"Broke ass\"\nOutput: " + booking.getReasonFailed());
        assertEquals(booking.getReasonFailed(), "Broke ass");
        booking.setReasonFailed("");
    }

    @Test
    void getPosition() {
        System.out.println("Test that the booking correctly stores the position\nFunction call: getPosition()\nExpected output: Front\nOutput: " + booking.getPosition() + "\n");
        assertEquals(booking.getPosition(), Position.Front);
    }

    @Test
    void setPosition() {
        System.out.println("Test that the position of a booking can be updated");
        Position oldPos = booking.getPosition();

        booking.setPosition(Position.Middle);
        assertEquals(booking.getPosition(), Position.Middle);

        booking.setPosition(oldPos);
    }

    @Test
    void setTransationTime() {
        LocalDateTime testTime = LocalDateTime.now().plusDays(1);
        booking.setTransationTime(testTime);
        assertEquals(testTime, booking.getTransationTime());
    }
}