package MovieAssingment;

import org.junit.jupiter.api.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CinemaTest {
    @BeforeAll
    static void beforeTests() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
    }

    @Test
    void addSessionTest() {
        Movie movie = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Session session = new Session(LocalDateTime.now(), movie, cin);
        cin.addSession(session);
        assertEquals(cin.getSessions().get(0), session);
    }

    @Test
    void removeSessionTest() {
        Movie movie = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Session session = new Session(LocalDateTime.now(), movie, cin);
        assertEquals(1, cin.getSessions().size());
        cin.removeSession(session);
        assertEquals(0, cin.getSessions().size());
    }

    @Test
    void getCinemaTypeTest() {
        /*assertEquals(cin.getCinema, )*/
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getCinemaType(), Cinema.CinemaType.Silver);

    }

    @Test
    void setCinemaType() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getCinemaType(), Cinema.CinemaType.Silver);
        cin.setCinemaType(Cinema.CinemaType.Gold);
        assertEquals(cin.getCinemaType(), Cinema.CinemaType.Gold);
    }

    @Test
    void getTicketPrice() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getTicketPrice(), 15);
    }

    @Test
    void setTicketPrice() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getTicketPrice(), 15);
        cin.setTicketPrice(25);
        assertEquals(cin.getTicketPrice(), 25);
    }

    @Test
    void getNumFrontSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumFrontSeats(), 10);
    }

    @Test
    void setNumFrontSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumFrontSeats(), 10);
        cin.setNumFrontSeats(100);
        assertEquals(cin.getNumFrontSeats(), 100);
    }

    @Test
    void getNumMiddleSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumMiddleSeats(), 10);
    }

    @Test
    void setNumMiddleSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumMiddleSeats(), 10);
        cin.setNumMiddleSeats(100);
        assertEquals(cin.getNumMiddleSeats(), 100);
    }

    @Test
    void getNumBackSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumBackSeats(), 10);
    }

    @Test
    void setNumBackSeats() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumBackSeats(), 10);
        cin.setNumBackSeats(100);
        assertEquals(cin.getNumBackSeats(), 100);
    }

    @Test
    void getSessions() {
        ArrayList<Session> sessions = new ArrayList<>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Movie movie = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));
        Session session = new Session(LocalDateTime.now(), movie, cin);
        cin.addSession(session);
        assertEquals(cin.getSessions().get(0), session);
    }

    @Test
    void getNumberofSeats(){
        ArrayList<Session> sessions = new ArrayList<Session>();
        Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        assertEquals(cin.getNumSeats(), 30);
        cin.setNumFrontSeats(50);
        assertEquals(cin.getNumSeats(), 70);
    }

    @Test
    void setSessions(){
        ArrayList<Session> sessions = new ArrayList<Session>();
        Movie movie1 = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));        
        Movie movie2 = new Movie("Mario Movie", Movie.Rating.G, "Mario and Luigi", LocalDate.parse("2001-11-29"), "Bowser",
                new ArrayList<String>(Arrays.asList("Chriss Pratt", "Emma Watson", "Lebron James")));        
                Cinema cin = new Cinema(Cinema.CinemaType.Silver, 15, 10, 10, 10, sessions);
        Session session1 = new Session(LocalDateTime.now(), movie1, cin);
        Session session2 = new Session(LocalDateTime.now(), movie2, cin);
        
        ArrayList<Session> collective = new ArrayList<Session>();
        collective.add(session1);
        collective.add(session2);

        cin.setSessions(collective);
        assertEquals(2, cin.getSessions().size());
    }
}