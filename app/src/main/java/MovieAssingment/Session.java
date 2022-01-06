package MovieAssingment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Session {
    private LocalDateTime sessionTime;
    private int numBookings = 0;
    private Movie movie;
    private Cinema cinema;
    private int availableFrontSeats;
    private int availableMiddleSeats;
    private int availableBackSeats;

    public Session(LocalDateTime sessionTime, Movie movie, Cinema cinema){
        this.sessionTime = sessionTime;
        this.movie = movie;
        this.movie.addSession(this);
        this.cinema = cinema;
        this.cinema.addSession(this);
        this.availableFrontSeats = cinema.getNumFrontSeats();
        this.availableMiddleSeats = cinema.getNumMiddleSeats();
        this.availableBackSeats = cinema.getNumBackSeats();
    }
    // "12:30 Tue 28/5   Harry Potter  %20        GOLD | Num bookings-  | Capacity- %d/%d"
    public String getSessionSum() {
        StringBuilder output = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm E dd/MM");
//        session.getSessionTime().format(formatter)
        output.append(getSessionTime().format(formatter));
        output.append(String.format(" | %-14s", movie.getTitle()));
        output.append(String.format("%-7s | Num bookings: %d | Capacity: %d/%d\n",
                cinema.getCinemaType().name(), numBookings, getAvailableSeats(), cinema.getNumSeats()));

        return output.toString();
    }



    public LocalDateTime getSessionTime() {
        return sessionTime;
    }
    
    public void setSessionTime(LocalDateTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getNumBookings() {
        return numBookings;
    }

    public void setNumBookings(int numBookings) {
        this.numBookings = numBookings;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public int getAvailableFrontSeats() {
        return availableFrontSeats;
    }

    public int getAvailableBackSeats() {
        return availableBackSeats;
    }

    public int getAvailableMiddleSeats() {
        return availableMiddleSeats;
    }

    public void setAvailableFrontSeats(int seats) {
        this.availableFrontSeats = seats;
    }

    public void setAvailableBackSeats(int seats) {
        this.availableBackSeats = seats;
    }

    public void setAvailableMiddleSeats(int seats) {
        this.availableMiddleSeats = seats;
    }

    public int getAvailableSeats() { return availableFrontSeats + availableMiddleSeats + availableBackSeats;}
}
