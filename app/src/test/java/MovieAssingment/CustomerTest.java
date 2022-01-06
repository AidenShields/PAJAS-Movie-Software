package MovieAssingment;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void getPassword() {
        Customer customer = new Customer("SuperUser", "SecurePass");
        assertEquals("SecurePass", customer.getPassword());
    }

    @Test
    void getUsername() {
        Customer customer = new Customer("SuperUser", "SecurePass");
        assertEquals("SuperUser", customer.getUsername());
    }

    @Test
    void getSavedCreditCard() {
        Customer customer = new Customer("SuperUser", "SecurePass");
        CreditCard card = new CreditCard("SuperUser", "55555");
        customer.addSavedCreditCard(card);
        assertEquals(card, customer.getSavedCreditCard());
    }

    @Test
    void getBookings() {
        Customer customer = new Customer("SuperUser", "SecurePass");

        ArrayList<Session> sessions = new ArrayList<Session>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Movie movie1 = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
        new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));        
        Session session = new Session(LocalDateTime.now(), movie1, cin);
        Booking book = new Booking("alpha", session, customer, true, "Cause lol", Position.Back, LocalDateTime.now());

        customer.addBooking(book);
        ArrayList<Booking> booking = new ArrayList<Booking>();

        booking.add(book);

        assertEquals(booking, customer.getBookings());
    }

    @Test
    void addBooking() {
        Customer customer = new Customer("SuperUser", "SecurePass");

        ArrayList<Session> sessions = new ArrayList<Session>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Movie movie1 = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
        new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));        
        Session session = new Session(LocalDateTime.now(), movie1, cin);
        Booking book = new Booking("alpha", session, customer, true, "Cause lol", Position.Back, LocalDateTime.now());

        customer.addBooking(book);

        assertEquals(1, customer.getBookings().size());
    }

    @Test
    void setUsername() {
        Customer customer = new Customer("SuperUser", "SecurePass");

        customer.setUsername("basic");

        assertNotEquals("SuperUser", customer.getUsername());
        assertEquals("basic", customer.getUsername());
    }

    @Test
    void setPassword() {
        Customer customer = new Customer("SuperUser", "SecurePass");

        customer.setPassword("lolcat");

        assertNotEquals("SecurePass", customer.getPassword());
        assertEquals("lolcat", customer.getPassword());
    }

    @Test
    void addSavedCreditCard() {
        Customer customer = new Customer("SuperUser", "SecurePass");
        CreditCard card = new CreditCard("SuperUser", "55555");
        customer.addSavedCreditCard(card);
        assertEquals(card, customer.getSavedCreditCard());
    }

    @Test
    void removeSavedCreditCard() {
        Customer customer = new Customer("SuperUser", "SecurePass");
        CreditCard card = new CreditCard("SuperUser", "55555");
        customer.addSavedCreditCard(card);
        assertEquals(card, customer.getSavedCreditCard());

        customer.removeSavedCreditCard();
        assertNotEquals(card, customer.getSavedCreditCard());
    }

    @Test
    void removeBooking() {
        Customer customer = new Customer("SuperUser", "SecurePass");

        ArrayList<Session> sessions = new ArrayList<Session>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Movie movie1 = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
        new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));        
        Session session = new Session(LocalDateTime.now(), movie1, cin);
        Booking book = new Booking("alpha", session, customer, true, "Cause lol", Position.Back, LocalDateTime.now());

        customer.addBooking(book);

        assertEquals(1, customer.getBookings().size());

        customer.removeBooking(book);

        assertEquals(0, customer.getBookings().size());
    }

    @Test
    public void loginTest(){
        Customer customer = new Customer("SuperUser", "SecurePass");

        assertTrue(customer.login("SuperUser", "SecurePass"));
        assertFalse(customer.login("Wrong user", "password"));
    }
}