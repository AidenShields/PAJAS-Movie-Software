package MovieAssingment;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MovieAssingment.Movie.Rating;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

class MovieTest {
    static Movie movie;
    @BeforeAll
    static void beforeTests() {
        movie = new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
        new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint")));
    }

    @Test
    void getInfo() {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Harry Potter");
        expected.add("PG");
        expected.add("You're a wizard Harry");
        expected.add("2001-11-29");
        expected.add("Chris Columbus");
        expected.add("Daniel Radcliffe, Emma Watson, Rupert Grint");
        System.out.println(movie.getInfo());
        assertEquals(movie.getInfo(), expected);

        movie.setRating(Rating.G);
        assertEquals(Rating.G, movie.getRating());

        movie.setTitle("James Bond");
        assertEquals("James Bond", movie.getTitle());

        movie.setDirector("Daniel");
        assertEquals("Daniel", movie.getDirector());

        movie.setCast(new ArrayList<String>(Arrays.asList("Jack", "Archie")));
        assertEquals(new ArrayList<String>(Arrays.asList("Jack", "Archie")), movie.getCast());

        movie.setSynopsis("It's James, James Bond");
        assertEquals("It's James, James Bond", movie.getSynopsis());

        movie.removeCastMember("Jack");
        assertEquals(new ArrayList<String>(Arrays.asList("Archie")), movie.getCast());

        movie.removeAllSessions();
        assertEquals(movie.getSessions(), new ArrayList<Session>());
    }

    // @Test
    // void setRating() {
    //     movie.setRating(Rating.G);
    //     assertEquals(Rating.G, movie.getRating());
    // }

    // @Test
    // void setTitle() {
    //     movie.setTitle("James Bond");
    //     assertEquals("James Bond", movie.getTitle());
    // }

    // @Test
    // void setDirector() {
    //     movie.setDirector("Daniel");
    //     assertEquals("Daniel", movie.getDirector());
    // }

    // @Test
    // void setCast() {
    //     movie.setCast(new ArrayList<String>(Arrays.asList("Jack", "Archie")));
    //     assertEquals(new ArrayList<String>(Arrays.asList("Jack", "Archie")), movie.getCast());
    // }

    // @Test
    // void setSynopsis() {
    //     movie.setSynopsis("It's James, James Bond");
    //     assertEquals("It's James, James Bond", movie.getSynopsis());
    // }
}