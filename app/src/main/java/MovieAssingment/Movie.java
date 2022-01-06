package MovieAssingment;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Arrays;


public class Movie {
    private String title;
    private Rating rating;
    private String synopsis;
    private LocalDate releaseDate;
    private String director;
    private ArrayList<String> cast = new ArrayList<String>();
    private ArrayList<Session> sessions = new ArrayList<Session>();

    public enum Rating {
        G,
        PG,
        M,
        MA,
        R
    }

    public Movie(String title, Rating rating, String synopsis, LocalDate releaseDate, String director, ArrayList<String> cast) {
        this.title = title;
        this.rating = rating;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.director = director;
        this.cast = cast;
    }

    public ArrayList<String> getInfo(){
        return new ArrayList<>(Arrays.asList(
                this.getTitle(),
                this.getRating() == null ? null : this.getRating().toString(),
                this.getSynopsis(),
                this.getReleaseDate() == null ? null : this.getReleaseDate().toString(),
                this.getDirector(),
                this.getCast() == null ? null : this.getCast().toString().replace("[", "").replace("]", "")
        ));
    }

    public void safeDeleteSessions() {
        for (Session sesh: this.getSessions()) {
            sesh.getCinema().removeSession(sesh);
        }

    }

    // Sessions stuff
    public ArrayList<Session> getSessions() {
        return this.sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public boolean boolRemoveSession(Session session) {
        return this.sessions.remove(session);
    }
    public void removeAllSessions() {
        this.sessions.clear();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = cast;
    }

    public void addCastMember(String cast){
        this.cast.add(cast);
    }

    public void removeCastMember(String cast){
        this.cast.remove(cast);
    }

}
