package MovieAssingment;
import java.util.ArrayList;


public class Customer {
    private String username;
    private String password;
    private CreditCard savedCreditCard;
    private ArrayList<Booking> bookings;

    public Customer(String username, String password){
        this.username = username;
        this.password = password;
        this.savedCreditCard = null;
        this.bookings = new ArrayList<Booking>();
    }

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public CreditCard getSavedCreditCard() {
        return savedCreditCard;
    }
    public ArrayList<Booking> getBookings() {
        return bookings;
    }
    public void addBooking(Booking booking) { this.bookings.add(booking); }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void addSavedCreditCard(CreditCard card){
        savedCreditCard = card;
    }
    public void removeSavedCreditCard(){
        savedCreditCard = null;
    }
    public void removeBooking(Booking booking){
        this.bookings.remove(booking);
    }

    public boolean login(String username, String password){
        if (this.username.equals(username) && this.password.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }
}