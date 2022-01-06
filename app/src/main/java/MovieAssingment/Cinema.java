package MovieAssingment;

import java.util.ArrayList;

public class Cinema {
    private CinemaType cinemaType;
    private float ticketPrice;
    private int numFrontSeats;
    private int numMiddleSeats;
    private int numBackSeats;
    private ArrayList<Session> sessions;


    public enum CinemaType {
        Bronze,
        Silver,
        Gold
    }

    public Cinema(CinemaType cinemaType, float ticketPrice, int numFrontSeats, int numMiddleSeats, int numBackSeats, ArrayList<Session> sessions) {
        this.cinemaType = cinemaType;
        this.ticketPrice = ticketPrice;
        this.numFrontSeats = numFrontSeats;
        this.numMiddleSeats = numMiddleSeats;
        this.numBackSeats = numBackSeats;
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }
    public void addSessions(ArrayList<Session> seshs) {
        this.sessions.addAll(seshs);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    public CinemaType getCinemaType() {
        return cinemaType;
    }

    public void setCinemaType(CinemaType cinemaType) {
        this.cinemaType = cinemaType;
    }

    public float getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getNumFrontSeats() {
        return numFrontSeats;
    }

    public void setNumFrontSeats(int numFrontSeats) {
        this.numFrontSeats = numFrontSeats;
    }

    public int getNumMiddleSeats() {
        return numMiddleSeats;
    }

    public void setNumMiddleSeats(int numMiddleSeats) {
        this.numMiddleSeats = numMiddleSeats;
    }

    public int getNumBackSeats() {
        return numBackSeats;
    }

    public void setNumBackSeats(int numBackSeats) {
        this.numBackSeats = numBackSeats;
    }

    public int getNumSeats() {return numFrontSeats + numMiddleSeats + numBackSeats;}

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
}
