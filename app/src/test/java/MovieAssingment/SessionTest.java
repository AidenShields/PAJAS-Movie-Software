package MovieAssingment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

class SessionTest {
    static Cinema silver;
    static Cinema gold;
    static Movie harry;
    static Movie squad;
    static Session session;

    @BeforeAll
    static void beforeAll() {
        silver = new Cinema(Cinema.CinemaType.Silver, (float) 20, 15, 15, 15, new ArrayList<Session>());
        gold = new Cinema(Cinema.CinemaType.Gold, (float) 40, 15, 15, 15, new ArrayList<Session>());
    
        harry = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));
        squad = new Movie("Suicide Squad", Movie.Rating.MA, "Death Squad", LocalDate.parse("2021-08-05"), "James Gunn",
                new ArrayList<String>(Arrays.asList("Sylvester Stallone", "Jai Courtney", "Viola Davis")));
    
        session = new Session(LocalDateTime.now(), harry, silver);
    
    }

    @Test
    void setMovie() {
        session.setMovie(squad);
        assertEquals(squad, session.getMovie());
    }

    @Test
    void setCinema() {
        session.setCinema(gold);
        assertEquals(gold, session.getCinema());
    }

    @Test
    void TestBackSeats() {
        assertEquals(15, session.getAvailableBackSeats());
        session.setAvailableBackSeats(20);
        assertEquals(20, session.getAvailableBackSeats());
    }

    @Test
    void TestMiddleSeats() {
        assertEquals(15, session.getAvailableMiddleSeats());
        session.setAvailableMiddleSeats(20);
        assertEquals(20, session.getAvailableMiddleSeats());
    }

}
