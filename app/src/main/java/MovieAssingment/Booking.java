package MovieAssingment;

import java.time.LocalDateTime;

enum Position{
    Front,
    Middle,
    Back
}

public class Booking {
    private String bookingID;
    private Session session;
    private Customer customer;
    private boolean bookingStatus;
    private String reasonFailed;
    private Position position;
    private LocalDateTime transactionTime;
    
    public Booking(String bookingID, Session session, Customer customer, boolean bookingStatus, String reasonFailed, Position position, LocalDateTime transactionTime) {
        this.bookingID = bookingID;
        this.session = session;
        this.customer = customer;
        this.bookingStatus = bookingStatus;
        this.reasonFailed = reasonFailed;
        this.position = position;
        this.transactionTime = transactionTime;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(boolean bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getReasonFailed() {
        return reasonFailed;
    }

    public void setReasonFailed(String reasonFailed) {
        this.reasonFailed = reasonFailed;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public LocalDateTime getTransationTime(){return this.transactionTime;}

    public void setTransationTime(LocalDateTime time){this.transactionTime = time;}


}
