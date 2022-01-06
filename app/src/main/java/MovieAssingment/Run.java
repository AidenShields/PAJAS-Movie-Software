package MovieAssingment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import MovieAssingment.Cinema.CinemaType;
import MovieAssingment.Movie.Rating;

import java.util.concurrent.*;


public class Run {
    ArrayList<Cinema> cinemaList = new ArrayList<Cinema>();
    ArrayList<Customer> customerList = new ArrayList<Customer>();
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
    ArrayList<GiftCard> giftCards = new ArrayList<GiftCard>();
    ArrayList<Booking> failedTransactions = new ArrayList<Booking>();
    String jsonFile;
    ArrayList<Booking> bookings = new ArrayList<Booking>();
    ArrayList<Staff> staff = new ArrayList<Staff>();
    Customer currentCustomer;
    Staff currentStaff;
    int totalBookings;

    public Run(ArrayList<Cinema> cinemas, ArrayList<Movie> movies){
        this.cinemaList = cinemas;
        this.movieList = movies;
    }

    public Run(){}

    public void parseJSON(String jsonFile) {
        JSONParser jsonParser = new JSONParser();
        HashMap<String, String> map = new HashMap<>();
        try (FileReader reader = new FileReader(jsonFile)) {
            Object obj = jsonParser.parse(reader);
            JSONArray creditCardJSON = (JSONArray) obj;
            for (Object value : creditCardJSON) {
                JSONObject newObject = (JSONObject) value;

                String cardHolderName = (String) newObject.get("name");
                String cardHolderNumber = (String) newObject.get("number");
                if (cardHolderNumber == null || cardHolderName == null)
                    continue;

                CreditCard card = new CreditCard(cardHolderName, cardHolderNumber);
                creditCards.add(card);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveJson(String jsonFile) {
        JSONArray listofCards = new JSONArray();
        for (int i = 0; i < creditCards.size(); i++) {
            JSONObject cardObject = new JSONObject();
            String name = creditCards.get(i).getCardHolderName();
            String number = creditCards.get(i).getNumber();
            cardObject.put("name", name);
            cardObject.put("number", number);
            listofCards.add(cardObject);
        }
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(listofCards.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJsonFile(){return null;}
    public void setJsonFile(String jsonFile){}


    public int getIntInputBookings(String prompt, int min, int max, Scanner scan){
        System.out.println(prompt);
        int output;
        String command;
        try {
            command = scan.nextLine();
            if(command.trim() == "") {
                return -4;
            }
            if (command.equals("q")) {
                return -2;
            } else {
                try {
                    output = Integer.parseInt(command);
                } catch (Exception e) {
                    return -1;
                }
                if (output > max || output < min) {
                    return -1;
                }
                return output;
            }
        } catch (Exception e) {
            System.out.println("q");
            return -3;
        }
    }

    public String displaySessionsList(ArrayList<Session> sessions) {
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm E dd/MM");

        for (Session session: sessions) {
            result.append(String.format("%-19s", session.getMovie().getTitle())).append(
                    String.format("%-17s", session.getSessionTime().format(formatter))).append(
                    String.format("%-5s\n", session.getCinema().getCinemaType()));
        }
        return result.toString();
    }

    public String displayMovieSessions(Movie movie) {
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm E dd/MM");

        result.append(movie.getTitle()).append(" -\n");
        for (Session session: movie.getSessions()) {
            result.append(String.format("\t%-17s", session.getSessionTime().format(formatter))).append(
                          String.format("%-5s\n", session.getCinema().getCinemaType()));
            }
        return result.toString();
    }

    public String displayAllSessions() {
        StringBuilder result = new StringBuilder();

        for (Movie movie: this.movieList) {
            result.append(displayMovieSessions(movie));
        }
        return result.toString();
    }

    public ArrayList<Movie> filterMovieByRating(Movie.Rating movieRating){
        ArrayList<Movie> sortedList = new ArrayList<Movie>();
        for(Movie movie: movieList){
            if(movie.getRating() == movieRating){
                sortedList.add(movie);
            }
        }
        return sortedList;
    }

    public ArrayList<Session> filterSessionByCinemaClass(Cinema.CinemaType cinemaType){
        ArrayList<Session> sortedList = new ArrayList<>();
        for(Cinema cinema: cinemaList){
            if(cinema.getCinemaType() == cinemaType){
                ArrayList<Session> moviesInCinema = cinema.getSessions();
                sortedList.addAll(moviesInCinema);
            }
        }
        return sortedList;
    }

    public ArrayList<Session> filterSessionByTime(ArrayList<Movie> movies, Integer day) {
        ArrayList<Session> allSessions = new ArrayList<Session>();

        for (Movie thisMovie: movies){
            if (day != null) {
                List<Session> sessionsOnDay = thisMovie.getSessions().stream().filter(
                        sesh -> sesh.getSessionTime().getDayOfWeek().getValue() == day).collect(Collectors.toList()
                );

                allSessions.addAll(new ArrayList<Session>(sessionsOnDay));
            } else {
                allSessions.addAll(thisMovie.getSessions());
            }
        }

        allSessions.sort(Comparator.comparing(Session::getSessionTime));
        return allSessions;
    }

    public String viewMovieInfo(Movie selectedMovie) {
        String str = "";
        str += "--------------------------------------";
        str += ("\n" + "Name:           " + selectedMovie.getTitle());
        str += ("\n" + "Rating:         " + selectedMovie.getRating());
        str += ("\n" + "Director:       " + selectedMovie.getDirector());
        str += ("\n" + "Release Date:   " + selectedMovie.getReleaseDate());
        str += ("\n" + "Movie Cast:     " + selectedMovie.getCast());
        str += ("\n" + "Movie Synopsis: " + selectedMovie.getSynopsis());
        str += ("\n" + "--------------------------------------");
        return str;
    }

    public void failTransaction(String reason) {
        Booking failedTransaction = new Booking(("" + (totalBookings)), null, currentCustomer, false, reason, null, LocalDateTime.now());
        totalBookings++;
        this.failedTransactions.add(failedTransaction);
    }

    public boolean bookASession() {
//        try {
//            boolean s = CompletableFuture.supplyAsync(() -> makeBooking())
//                    .get(5, TimeUnit.SECONDS);
//        } catch (TimeoutException e) {
//            System.out.println("Timeout has occurred, returning to the home page\n");
//            return false;
//        } catch (InterruptedException | ExecutionException e) {
//            return false;
//        }
        return true;
    }

    public boolean makeBooking(Scanner scan){

        System.out.println("========================================================================\n========================================================================\n   WELCOME TO THE BOOKING INTERFACE ... ENTER q TO LEAVE AT ANY TIME\n========================================================================\n========================================================================");


        //Allow Customer to select a movie
        System.out.println("There are currently " + movieList.size() + " movies showing");

        if(movieList.size() == 0) {return false;} //cheeky Edge case

        //loop through movies and print all, ask for input in terms of index
        for(int i = 0; i < movieList.size(); i++){
            System.out.println("Movie: " + i);
            Movie movie = movieList.get(i);
            System.out.println(viewMovieInfo(movie));
            System.out.println();
        }

        int movieSelectionInt;
        while(true) {
            movieSelectionInt = getIntInputBookings("Select your movie based on its number value (Between 0 - " + (movieList.size() - 1) + "):", 0, movieList.size() - 1, scan);
            if(movieSelectionInt == -1) {
                System.out.println("Invalid input ... Please try again");
                continue;
            }
            else if(movieSelectionInt == -2) {
                failTransaction("User cancelled");
                return false;
            }
            else if(movieSelectionInt == -3) {
                failTransaction("Timeout");
                return false;
            }
            else if(movieSelectionInt == -4) {
                failTransaction("Timeout");
                return false;
            }
            else {
                break;
            }
        }
        //set movie and list of movie sessions
        ArrayList<Session> selectedMovieSessions = new ArrayList<>();
        Movie movieSelected = movieList.get(movieSelectionInt);
        System.out.println("You have selected: \n" + viewMovieInfo(movieSelected));

        //iterate through da lists to find the sessions
        for(Cinema cinema: cinemaList){
            ArrayList<Session> cinemaSession = cinema.getSessions();
            for(Session session: cinemaSession){
                if(session.getMovie() == movieSelected){
                    selectedMovieSessions.add(session);
                }
            }
        }

        //display the sessions
        for(int i = 0; i < selectedMovieSessions.size(); i++){
            Session session = selectedMovieSessions.get(i);
            System.out.println("Option: " + i);
            System.out.println(session.getMovie().getTitle() + ": " + session.getSessionTime() + " (" +  session.getCinema().getCinemaType() + ")\n");
        }

        int sessionSelectionIndex;
        while(true) {
            sessionSelectionIndex = getIntInputBookings("Select your session based on its number value (Between 0 - " + (selectedMovieSessions.size() - 1)+ "):", 0, selectedMovieSessions.size() - 1, scan);
            if(sessionSelectionIndex == -1) {
                System.out.println("Invalid input ... please try again");
                continue;
            }
            else if(sessionSelectionIndex == -2) {
                failTransaction("User cancelled");
                return false;
            }
            else if(movieSelectionInt == -3) {
                failTransaction("Timeout");
                return false;
            }
            else {
                break;
            }
        }

        Session selectedSession = selectedMovieSessions.get(sessionSelectionIndex);
        int tickets = 0;
        int bookedFront = 0;
        int bookedMiddle = 0;
        int bookedBack = 0;
        int bill = 0;

        while(true) {
            if((bookedFront == selectedSession.getAvailableFrontSeats()) && (bookedMiddle == selectedSession.getAvailableMiddleSeats()) && (bookedBack == selectedSession.getAvailableBackSeats())) {
                System.out.println("You literally booked the whole damn cinema ... proceeding to checkout");
                break;
            }
            int continueStatus;
            if(tickets != 0) {
                while(true) {
                    continueStatus = getIntInputBookings("You currently have " + tickets + " tickets booked. Would you like to:\n 1 - Book more tickets\n 2 - Proceed to checkout", 1, 2, scan);
                    if(continueStatus == -1) {
                        System.out.println("Invalid input ... please try again");
                        continue;
                    }
                    else if(continueStatus == -2) {
                        failTransaction("User cancelled");
                        return false;
                    }
                    else if(movieSelectionInt == -3) {
                        failTransaction("Timeout");
                        return false;
                    }
                    else {
                        break;
                    }
                }
                if(continueStatus == 2) {
                    break;
                }
            }

            System.out.println("Where would you like to sit?");
            System.out.println("Front: " + (selectedSession.getAvailableFrontSeats() - bookedFront));
            System.out.println("Middle: " + (selectedSession.getAvailableMiddleSeats() - bookedMiddle));
            System.out.println("Back: " + (selectedSession.getAvailableBackSeats() - bookedBack));

            int position;
            while(true) {
                position = getIntInputBookings("Enter seating position\n 1 - Front\n 2 - Middle\n 3 - Back", 1, 3, scan);
                if(position == -1) {
                    System.out.println("Invalid input ... please try again");
                    continue;
                }
                else if(position == -2) {
                    failTransaction("User cancelled");
                    return false;
                }
                else if(movieSelectionInt == -3) {
                    failTransaction("Timeout");
                    return false;
                }
                else {
                    break;
                }
            }
            switch(position) {
                case 1:
                    bookedFront += 1;
                    if(bookedFront > selectedSession.getAvailableFrontSeats()) {
                        System.out.println("Cannot book anymore front seats, please choose a valid seating position");
                        continue;
                    }
                    break;
                case 2:
                    bookedMiddle += 1;
                    if(bookedFront > selectedSession.getAvailableMiddleSeats()) {
                        System.out.println("Cannot book anymore middle seats, please choose a valid seating position");
                        continue;
                    }
                    break;

                case 3:
                    bookedBack += 1;
                    if(bookedFront > selectedSession.getAvailableBackSeats()) {
                        System.out.println("Cannot book anymore back seats, please choose a valid seating position");
                        continue;
                    }
                    break;

            }

            int ticketStatus;
            while(true) {
                ticketStatus = getIntInputBookings("What status is the customer this ticket is for?\n 1 - Youth (under 12) - " + Math.ceil((0.7 * selectedSession.getCinema().getTicketPrice())) + "\n 2 - Student - " + Math.ceil((0.8 * selectedSession.getCinema().getTicketPrice())) + "\n 3 - Senior (65+) - " + Math.ceil((0.9 * selectedSession.getCinema().getTicketPrice())) + "\n 4 - Adult - " + selectedSession.getCinema().getTicketPrice(), 1, 4, scan);
                if(ticketStatus == -1) {
                    System.out.println("Invalid input ... please try again");
                    continue;
                }
                else if(ticketStatus == -2) {
                    failTransaction("User cancelled");
                    return false;
                }
                else if(movieSelectionInt == -3) {
                    failTransaction("Timeout");
                    return false;
                }
                else {
                    break;
                }
            }

            switch(ticketStatus) {
                case 1:
                    bill += (int) Math.ceil((0.7 * selectedSession.getCinema().getTicketPrice()));
                    break;
                case 2:
                    bill += (int) Math.ceil((0.8 * selectedSession.getCinema().getTicketPrice()));
                    break;
                case 3:
                    bill += (int) Math.ceil((0.9 * selectedSession.getCinema().getTicketPrice()));
                    break;
                case 4:
                    bill += (int) selectedSession.getCinema().getTicketPrice();
                    break;
            }

            tickets++;
        }

        int paymentSelection;
        while(true) {
            paymentSelection = getIntInputBookings("How would you like to pay?\n 1 - Credit Card \n 2 - Gift Card", 1, 2, scan);

            if(paymentSelection == -1) {
                System.out.println("Invalid input ... please try again");
                continue;
            }
            else if(paymentSelection == -2) {
                failTransaction("User cancelled");
                return false;
            }
            else if(movieSelectionInt == -3) {
                failTransaction("Timeout");
                return false;
            }
            else {
                break;
            }
        }
        if(paymentSelection == 1){
            System.out.println("Charging you $" + bill + " by card");
            if(paymentCreditCard()) {
                System.out.println("Successfully charged!\nEnjoy your movie :)");
                generateBookings(selectedSession, bookedFront, bookedMiddle, bookedBack, tickets);
                return true;
            }
            else {
                bookingFailure(scan);
            }
        }
        else if(paymentSelection == 2) {
            System.out.println("Charging you $" + bill + " by gift card");
            if(paymentGiftCard()) {
                System.out.println("Successfully charged!\nEnjoy your movie :)");
                generateBookings(selectedSession, bookedFront, bookedMiddle, bookedBack, tickets);
                return true;
            }
            else {
                bookingFailure(scan);
            }
        }
        return false;
    }

    public void generateBookings(Session session, int numFront, int numMiddle, int numBack, int numTickets) {
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        //tickets will sum from each number
        for(int i = 0; i < numTickets; i++) {
            if(numFront == 0) {
                if(numMiddle == 0) {
                    Booking success = new Booking(("" + totalBookings), session, currentCustomer, true, "", Position.Back, LocalDateTime.now());
                    totalBookings++;
                    bookings.add(success);
                    continue;
                }
                else {
                    Booking success = new Booking(("" + totalBookings), session, currentCustomer, true, "", Position.Middle, LocalDateTime.now());
                    totalBookings++;
                    bookings.add(success);
                    continue;
                }
            }
            else {
                Booking success = new Booking(("" + totalBookings), session, currentCustomer, true, "", Position.Front, LocalDateTime.now());
                totalBookings++;
                bookings.add(success);
                continue;
            }
        }
        for(int i = 0; i < bookings.size(); i++) {
            currentCustomer.addBooking(bookings.get(i));
            this.bookings.add(bookings.get(i));
        }
    }

    public void bookingFailure(Scanner scan){
        failTransaction("Payment failure .. Broke ass");
        int selection;
        while(true) {
            selection = getIntInputBookings("Booking Process has failed due to incorrect Input\n Enter 1 to try book again\n Enter 2 to exit", 1, 2, scan);
            if(selection == -1) {
                System.out.println("Invalid input ... please try again");
                continue;
            }
            else if(selection == -2) {
                return;
            }
            else {
                break;
            }
        }
        if(selection == 1){
            bookASession();
        } else{
            System.exit(0);
        }
    }

    // Cinema
    public void addCinema(Cinema cinema){cinemaList.add(cinema);}
    public boolean removeCinema(Cinema cinema){
        return cinemaList.remove(cinema);
    }
    public ArrayList<Cinema> getCinemaList(){return this.cinemaList;}

    public void addCustomer(Customer customer){
        this.customerList.add(customer);
    }

    // Movie
    public ArrayList<Movie> getMovieList(){ return this.movieList;}
    public void addMovie(Movie movie){movieList.add(movie);}
    public void removeMovie(Movie movie){}

    public void editMovie(Movie movie){}


    // Customer
    public ArrayList<Customer> getCustomerList(){return this.customerList;}

    public void removeCustomer(Customer customer){this.customerList.remove(customer);}

    public Customer getCurrentCustomer(){
        return this.currentCustomer;
    }

    public void setCurrentCustomer(Customer customer){
        if (currentCustomer != null || currentStaff != null) {
            return;
        }
        this.currentCustomer = customer;
    }

    public void removeCurrentCustomer(){
        this.currentCustomer = null;
    }


    // Credit Card
    public void addCreditCard(CreditCard card){
        this.creditCards.add(card);
    }

    public void removeCreditCard(CreditCard card){
        this.creditCards.remove(card);
    }

    public CreditCard checkCardDetails(String number, String name){
        for (CreditCard cc : this.creditCards){
            if(cc.getCardHolderName().equalsIgnoreCase(name) && cc.getNumber().equals(number)){
                return cc;
                // number and name matches card in database
            }
        }
        return null;
    }

    // Assuming that this function is only accessible once logged in
    public boolean paymentCreditCard(){
        System.out.println("Payment by Credit Card selected (enter q to exit): ");

        // If customer has a saved credit card
        if (this.currentCustomer.getSavedCreditCard() != null){
            String CCnumber = this.currentCustomer.getSavedCreditCard().getNumber().substring(3);
            System.out.println("Using saved credit card ***" + CCnumber);
            // Transaction success
            return true;
        }

        // If customer does not have a saved credit card
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Insert cardholder's name: ");

            String cardHolderName = scanner.nextLine();

            if (cardHolderName.equalsIgnoreCase("q")){
                System.out.println("Transaction cancelled.");
                // scanner.close();
                return false;
            }

            System.out.println("\nInsert card number: ");

            String cardnumber = scanner.nextLine();
            if (cardnumber.equalsIgnoreCase("q")){
                System.out.println("Transaction cancelled.");
                // scanner.close();
                return false;
            }

            if (cardnumber.length() != 5){
                System.out.println("Invalid card number length.");
                System.out.println("Please try again.\n");
                continue;
            }


            
            try {
                int test = Integer.parseInt(cardnumber);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid card number entry. Please insert numeric input");
                System.out.println("Please try again.\n");
                continue;
            }
            
            CreditCard cc = this.checkCardDetails(cardnumber, cardHolderName);
                
            if (cc == null){
                System.out.println("Invalid card input. Card holder name or card number details are incorrect");
                System.out.println("Please try again.\n");
                continue;
            }
            else {
                System.out.println("Would you like to save your credit card details (Y/n)?");

                String response = scanner.nextLine();
                
                if (response.equalsIgnoreCase("y")){
                    this.currentCustomer.addSavedCreditCard(cc);
                    System.out.println("Credit card details have been saved.");
                }
                else if (response.equalsIgnoreCase("n")){
                    System.out.println("Credit card details were not saved.");
                }
                else if (response.equalsIgnoreCase("q")){
                    System.out.println("Transaction cancelled.");
                    // scanner.close();
                    return false;
                }

                //Success
                System.out.println("Transaction success.");
                // scanner.close();
                return true;
            }
        }
    }

    // Gift Card
    public int addGiftCard(GiftCard giftCard){
        if(giftCard.getGiftCardNumber().length() != 18 || ! giftCard.getGiftCardNumber().substring(0, 2).equals("GC")){
            return -1;
            // Invalid gift card format
        }

        for(GiftCard gc : this.giftCards){
            if (gc.getGiftCardNumber().equals(giftCard.getGiftCardNumber())){
                return -2;
                // gift card already exists
            }
        }

        this.giftCards.add(giftCard);
        return 0;
    }

    public void removeGiftCard(GiftCard giftCard){
        this.giftCards.remove(giftCard);
    }

    public ArrayList<GiftCard> getGiftCardList(){
        return this.giftCards;
    }

    public boolean paymentGiftCard(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Payment by gift Card selected (input q to exit): ");

        while(true){
            System.out.println("Insert gift card number: ");
            String giftCardNumber = scanner.nextLine();

            if(giftCardNumber.equalsIgnoreCase("q")){
                System.out.println("Transaction cancelled.");
                return false;
            }

            if(giftCardNumber.length() != 18 && ! giftCardNumber.substring(0, 2).equals("GC")){
                System.out.println("Invalid gift card format.\nEnsure gift card number is 16 digits in length and includes GC suffix.\n");
                continue;
                // Invalid gift card format
            }

            else {        
                for (GiftCard gc : this.giftCards){
                    if (gc.getGiftCardNumber().equals(giftCardNumber) && !gc.isRedeemed()){
                        System.out.println("Gift card accepted.");
                        gc.redeem();
                        System.out.println("Transaction succesful");
                        return true;
                    }
                }
                System.out.println("Gift card does not exist in the database or has already been redeemed.");
                System.out.print("Please insert valid gift card details.\n");
                continue;
                //Gift card does not exist in database
            }
        }
    }

    // Staff - Report generators
    public String getUpcomingSessions(Run dRun) {
        // A list of the upcoming movies & shows that include the item details.
        String spacer = "--------------------------------------";
        StringBuilder output = new StringBuilder();

        for (Movie mov: dRun.getMovieList()) {
            output.append(viewMovieInfo(mov)).append("\n");
            if (mov.getSessions().size() == 0) {
                output.append("    No sessions currently for this movie\n");
            } else {
                for (Session sesh: mov.getSessions()){
                    LocalDateTime seshTime = sesh.getSessionTime();
                    output.append("  ")
                            .append(String.format("%-15s", seshTime.format(DateTimeFormatter.ofPattern("H:m E d/M"))))
                            .append(" | ")
                            .append(sesh.getCinema().getCinemaType().name())
                            .append("\n");
                }
                output.append(spacer).append("\n");
            }
        }
        return output.toString();
    }

    public String getSessionsSummary(Run dRun){
        // A summary that includes number of bookings for each movie session as well as how many seats have been booked and are available for each session.
        StringBuilder output = new StringBuilder();

        ArrayList<Session> sortedSessions = new ArrayList<>();
        for (Cinema cin: dRun.getCinemaList()) {
            sortedSessions.addAll(cin.getSessions());
        }

        sortedSessions.sort(Comparator.comparing(Session::getSessionTime));
        int counter = 1;
        for (Session sesh: sortedSessions) {
            output.append(sesh.getSessionSum());

            if (counter % 5 == 0) {output.append("\n");}
            counter++;
        }
        return output.toString();
    }

    public static String printStaffMovieUI(Movie movie, int pos) {
        StringBuilder output = new StringBuilder();
        ArrayList<String> param = new ArrayList<>(Arrays.asList(
                "title", "rating", "synopsis", "releaseDate", "director", "cast"
        ));

        ArrayList<String> movieInfo = movie.getInfo();

        // Print current information
        output.append("\n\nCurrent movie information: (q to leave, *n to edit line n)\n");
        for (int i = 0; i < param.size(); i++) {
            if (i <= pos && movieInfo.get(i) != null) {
                output.append(String.format("%d: %-12s %s\n", (i + 1), param.get(i), movieInfo.get(i)));
            }
        }
        return output.toString();
    }

    private static Movie getMovieSelection(Scanner scan, ArrayList<Movie> movies) {

        for (int i = 0; i < movies.size(); i++) {
            System.out.format("%d - %s\n", i + 1, movies.get(i).getTitle());
        }

        String input;
        int selection = 0;
        while (selection == 0) {
            input = scan.nextLine();
            try {
                selection = Integer.parseInt(input);
                if (selection < 1 || selection > movies.size()) {
                    throw new Exception();
                }
                return movies.get(selection - 1);
            } catch (Exception e) {
                selection = 0;
                System.out.println("Please enter a valid selection between 1 and " + movies.size());
            }
        }
        return null;
    }

    public Movie staffAddEditMovie(Movie editMovie) {
        Scanner scan = new Scanner(System.in);
        String title = null;
        Movie.Rating rating = null;
        String synopsis = null;
        LocalDate releaseDate = null;
        String director = null;
        ArrayList<String> cast = null;


        int curPos = 0;
        boolean editMode = false;
        int editPosSave = 0;

        Movie movie;
        if (editMovie == null) {
            movie = new Movie(null, null, null, null, null, null);
        } else {
            movie = editMovie;
            curPos = 6;
        }

        ArrayList<String> questions = new ArrayList<>(Arrays.asList(
                "Please enter the movie title: ",
                "Please enter the movie rating (G, PG, M, MA, R): ",
                "Please enter the movie's synopsis: ",
                "Please enter the movie's releaseDate ( DD/MM/YYYY ): ",
                "Please enter the movie's director: ",
                "Please enter the movie's cast separated by commas (eg. steve,kevin,...): ",
                "Are you happy with this information, type 'y' to confirm or 'q' to cancel? (type *n to edit line n)"
        ));

        String input = null;

        while (true) {
            System.out.println(printStaffMovieUI(movie, curPos));

            System.out.println(questions.get(curPos));
            input = scan.nextLine();

            switch (input) {
                case "q": return null;
                case "*1": editMode = true;editPosSave = curPos;curPos = 0;continue;
                case "*2": editMode = true;editPosSave = curPos;curPos = 1;continue;
                case "*3": editMode = true;editPosSave = curPos;curPos = 2;continue;
                case "*4": editMode = true;editPosSave = curPos;curPos = 3;continue;
                case "*5": editMode = true;editPosSave = curPos;curPos = 4;continue;
                case "*6": editMode = true;editPosSave = curPos;curPos = 5;continue;
                case "":
                    System.out.println("Please provide some input (q to return, *num to edit line n [1-6]");
                    continue;
            }

            // Enter movie title
            if (curPos == 0) {
                input = input.trim();
                boolean duplicate = false;
                for(Movie film : movieList) {
                    if(film.getTitle().equals(input)) {
                        duplicate = true;
                        break;
                    }
                }
                if(duplicate) {
                    System.out.println("Movie with this title already exists");
                    continue;
                }
                movie.setTitle(input);
                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                } else {
                    curPos++;
                }
                continue;
            }

            // Enter movie rating
            if (curPos == 1) {
                input = input.toLowerCase();

                switch (input) {
                    case "g":
                        rating = Movie.Rating.G;
                        break;
                    case "pg":
                        rating = Movie.Rating.PG;
                        break;
                    case "m":
                        rating = Movie.Rating.M;
                        break;
                    case "ma":
                        rating = Movie.Rating.MA;
                        break;
                    case "r":
                        rating = Movie.Rating.R;
                        break;
                    default:
                        input = null;
                }
                if (input == null) {
                    continue;
                }

                movie.setRating(rating);
                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                } else {
                    curPos++;
                }
                continue;
            }

            // Enter movie synopsis
            if (curPos == 2) {
                movie.setSynopsis(input);
                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                } else {
                    curPos++;
                }
                continue;
            }

            // Enter movie releaseDate
            if (curPos == 3) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                try {
                    releaseDate = LocalDate.parse(input, formatter);
                    LocalDate currentDate = LocalDate.now();
                    if(releaseDate.compareTo(currentDate) > 0) {
                        releaseDate = null;
                        System.out.println("Please provide a valid date in the corresponding format (dd/mm/yyyy) that is not in the future");
                    }
                    movie.setReleaseDate(releaseDate);
                } catch (DateTimeParseException e) {
                    releaseDate = null;
                    System.out.println("Please provide a valid date in the corresponding format (dd/mm/yyyy)");
                }

                if (releaseDate == null) {
                    continue;
                }

                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                } else {
                    curPos++;
                }
                continue;
            }

            // Enter movie director
            if (curPos == 4) {
                movie.setDirector(input);
                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                } else {
                    curPos++;
                }
                continue;
            }

            // Enter movie cast
            if (curPos == 5) {
                try {
                    cast = new ArrayList<String>(Arrays.asList(input.trim().split(",")));
                    movie.setCast(cast);
                } catch (Exception e) {
                    cast = null;
                }
                if (cast == null) {
                    continue;
                }

                if (editMode) {
                    curPos = editPosSave;
                    editMode = false;
                    continue;
                } else {
                    curPos++;
                    System.out.println("6 cast: " + cast);
                }
            }

            if (curPos == 6) {
                if (input.toLowerCase().equals("y")) {
                    return movie;
                } else {
                    continue;
                }
            }
            return null;
        }
    }

    public static Session staffAddSession(Run dRun) {
        Scanner scan = new Scanner(System.in);
        String input;
        Movie sessionMovie = null;
        LocalDateTime sessionTime = null;
        Cinema sessionCinema = null;
        int curPos = 0;
        boolean editMode = false;
        int editPosSave = 0;

        System.out.println("\n\tCreating a new session -");

        while(true) {
            System.out.println("\nSelect a movie:");
            sessionMovie = getMovieSelection(scan, dRun.getMovieList());
            if (sessionMovie == null) {
                System.out.println("Failed to select a movie, please try again.");
            } else {
                break;
            }
        }


        DateTimeFormatter formatParse = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        DateTimeFormatter formatPrint = DateTimeFormatter.ofPattern("HH:mm E dd/MM");
        while(true) {
            System.out.println("\nEnter session date and time (MM/dd hh:mm): [q to cancel]");
            input = scan.nextLine();
            if (Objects.equals(input, "q")) {
                return null;
            }
            try {
                String[] dateAndTime = input.split(" ");
                if (dateAndTime.length != 2) {
                    throw new Exception();
                }

                sessionTime = LocalDateTime.parse("2021/" + input, formatParse);

                // TODO: need to ensure that this session is only within the current week
                LocalDateTime dateTime = LocalDateTime.now();
                LocalDateTime nextMonday = dateTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime recentMonday = nextMonday.minusWeeks(1);
                LocalDateTime current = LocalDateTime.now();

                if (sessionTime.isAfter(recentMonday) && sessionTime.isBefore(nextMonday) && (sessionTime.compareTo(current) > 0)) {
                    break;
                } else {
                    System.out.println("Sessions can only be within the current week and later than the current date + time\nplease re-enter the date (m/d) and time (h:m) within this week");
                }

            } catch (Exception e) {
                System.out.println("Please provide a valid date session in the format (MM/dd hh:mm)");
            }
        }

        System.out.println(sessionMovie);
        System.out.println(sessionTime);

        // Select a cinema
        while(true) {
            System.out.println("\nSelect a cinema: [q to cancel]");
            int count = 1;
            for (Cinema cin: dRun.cinemaList) {
                System.out.println(count + " - " + cin.getCinemaType().name());
                count++;
                for (Session sesh: cin.getSessions()) {
                    System.out.printf("\t%6s %s%n", sesh.getSessionTime().format(formatPrint), sesh.getMovie().getTitle());
                }
                System.out.println();
            }

            int selection = 0;
            while (selection == 0) {
                try {
                    input = scan.nextLine();
                    if (Objects.equals(input, "q")) {
                        return null;
                    }
                    selection = Integer.parseInt(input);
                    if (selection < 1 || selection > count) {
                        selection = 0;
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Please provide a integer selection between 1 and " + (count-1));
                    continue;
                }


                // Check there aren't overlapping sessions
                for (Session sesh: dRun.cinemaList.get(selection - 1).getSessions()) {
                    if (sesh.getSessionTime().isAfter(sessionTime.minusHours(2))
                            && sesh.getSessionTime().isBefore(sessionTime.plusHours(2))) {
                        // Session has an overlapping point
                        System.out.printf("Your selected cinema has an overlapping session - %-7s %s\n",
                                sesh.getSessionTime().format(formatPrint),
                                sesh.getMovie().getTitle());
                        selection = 0;
                        break;
                    }
                }
            }
            sessionCinema = dRun.getCinemaList().get(selection - 1);
            break;
        }

        // Confirm session or cancel
        while (true) {
            System.out.println("\nAdd the new session bellow? y for yes, n/q for cancel");
            System.out.printf("%s (%s) - %s\n", sessionTime.format(formatPrint), sessionCinema.getCinemaType().name(), sessionMovie.getTitle());

            input = scan.nextLine();
            switch (input.toLowerCase()){
                case "y":
                    return new Session(sessionTime, sessionMovie, sessionCinema);

                case "n":
                case "q":
                    return null;
                default:
            }
        }
    }

    public static void staffAddGiftCard(Run dRun) {
        Scanner scan = new Scanner(System.in);

        String input;
        while (true) {
            System.out.println("\nEnter new GiftCard to be activated ( GC***************** - 16 digit ), q to cancel");
            input = scan.nextLine();
            if (input.equalsIgnoreCase("q")) {
                return;
            }
            int ret = dRun.addGiftCard(new GiftCard(input));
            if (ret == -1) {
                System.out.println("Incorrect format, please enter GC then 16 digit code.");
            } else if (ret == -2) {
                System.out.println("GiftCard already in the database, please try again with a new card.");
            } else {
                System.out.println("GiftCard successfully added.");
                break;
            }
        }
    }

    public Staff getCurrentStaff(){
        return this.currentStaff;
    }

    public void setCurrentStaff(Staff staff){
        if (currentCustomer != null || currentStaff != null) {
            return;
        }
        this.currentStaff = staff;
    }

    public void removeCurrentStaff(){
        this.currentStaff = null;
    }

    //manager only methods
    public String getFailedTransactions() {
        if(this.currentStaff == null) {
            //Unreachable error
            return "";
        }
        else if(!this.currentStaff.isManager()){
            return "Cannot view failed transactions without manager status\n";
        }
        // A list of the upcoming movies & shows that include the item details.
        String spacer = "--------------------------------------";
        StringBuilder output = new StringBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm E dd/MM");

        output.append("Booking ID   Time Date   Customer   Reason\n");
        for (Booking booking: this.failedTransactions) {
            String customer;
            if (booking.getCustomer() != null && booking.getCustomer().getUsername() != null) {
                customer = booking.getCustomer().getUsername();
            } else {
                customer = "anonymous";
            }
            // This summary only includes date and time of the cancelled, the user (if available, otherwise "anonymous") and the reasons (e.g. "timeout", "user cancelled", "card payment failed", etc.)
            output.append(String.format("%-7s %-7s %-15s %-12s\n",
                    booking.getBookingID(),
                    booking.getTransationTime().format(format),
                    customer,
                    booking.getReasonFailed()
            ));
        }

        return output.toString();
    }

    // for the system to add inital staff members
    public void addStaffMember(Staff staff){
        this.staff.add(staff);
    }
    // for the system to add inital staff members
    public void removeStaffMemeber(Staff staff){
        this.staff.remove(staff);
    }

    public ArrayList<Staff> getStaffMembers(){
        return this.staff;
    }

    public void managerAddStaffMember(){
        if (this.currentCustomer != null){
            System.out.println("Insufficient access level as customer");
            return;
        }

        else if (this.currentStaff == null){
            System.out.println("Insufficient access level as guest user");
            return;
        }

        else if (! this.currentStaff.isManager()){
            System.out.println("Insufficient access level as regular staff member");
            return;
        }


        System.out.println("Enter q to cancel");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter a username for the staff member: ");
        String username = "";

        try {
            username = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (username.equals("")) {
            System.out.println("No username was input");
            return;
        } else if (username.equals("q")) {
            System.out.println("Staff member registration cancelled");
            return;
        }

        for (Customer c : this.customerList) {
            if (c.getUsername().toLowerCase().equals(username.toLowerCase())) {
                System.out.println("\nUsername already exists.");
                return;
            }
        }
        for (Staff s : this.staff) {
            if (s.getUsername().toLowerCase().equals(username.toLowerCase())) {
                System.out.println("\nUsername already exists.");
                return;
            }
        }

        EraserThread et = new EraserThread();
        System.out.println("Create a password: ");
        String password = this.readPassword(et, in);

        if (password.equals("")) {
            System.out.println("\bNo password entered");
            return;
        } else if (password.equals("q")) {
            System.out.println("\bStaff member registration cancelled");
            return;
        }

        System.out.println("\bIs this staff member a manager (y/n): ");
        
        while(true){
            String s = "";

            try {
                s = in.readLine();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (s.toLowerCase().equals("y")){
                Staff staff = new Staff(username, password, true);
                System.out.println("Manager successfully created and added to the system!\nThank you!");
                this.staff.add(staff);
                return;
            }

            else if (s.toLowerCase().equals("n")){
                Staff staff = new Staff(username,password, false);
                System.out.println("Staff member successfuly created and added to the system!\nThank you!");
                this.staff.add(staff);
                return;
            }

            else if (s.toLowerCase().equals("q")){
                System.out.println("Staff member registration cancelled");
                return;
            }
            else{
                System.out.println("Invalid input. Please enter (y/n) for a manager account (y) or q to exit");
                continue;
            }
        }
    }

    public void managerRemoveStaffMember(){
        if (this.currentCustomer != null){
            System.out.println("Insufficient access level as customer");
            return;
        }

        else if (this.currentStaff == null){
            System.out.println("Insufficient access level as guest user");
            return;
        }

        else if (! this.currentStaff.isManager()){
            System.out.println("Insufficient access level as regular staff member");
            return;
        }


        System.out.println("Enter q to cancel");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter the username of the staff member you wish to remove: ");
        String username = "";

        try {
            username = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        for (Staff staff : this.staff){
            if (staff.getUsername().equals(username)){
                System.out.println("Staff member found");

                if (this.currentStaff == staff){
                    System.out.println("Cannot delete yourself");
                    return;
                }

                while(true){
                    System.out.println(String.format("Are you sure you want to delete member %s (y/n)?", username));
                    String s = "";

                    try {
                        s = in.readLine();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
        
                    if (s.toLowerCase().equals("y")){
                        this.staff.remove(staff);
                        System.out.println(String.format("Staff member, %s, successfully removed", username));
                        return;
                    }
        
                    else if (s.toLowerCase().equals("n") || s.toLowerCase().equals("q")){
                        System.out.println("Removal of staff member cancelled");
                        return;
                    }
                    else{
                        System.out.println("Invalid input. Please enter (y/n) or q to exit\n");
                        continue;
                    }
                }
            }
        }

        System.out.println("Staff member does not exist");
        return;
    }

    //login and registration
    public void registration() {
        this.currentCustomer = null;
        this.currentStaff = null;


        System.out.println("Enter q to cancel");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter a username: ");
        
        String username = "";

        try {
            username = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (username.equals("")) {
            System.out.println("No username was input");
            return;
        } else if (username.equals("q")) {
            System.out.println("Registration cancelled");
            return;
        }

        for (Customer c : this.customerList) {
            if (c.getUsername().toLowerCase().equals(username.toLowerCase())) {
                System.out.println("\nUser already exists.");
                return;
            }
        }
        for (Staff s : this.staff) {
            if (s.getUsername().toLowerCase().equals(username.toLowerCase())) {
                System.out.println("\nUser already exists.");
                return;
            }
        }


        EraserThread et = new EraserThread();
        System.out.println("Create a password: ");
        String password = this.readPassword(et, in);

        if (password.equals("")) {
            System.out.println("\bNo password entered");
            return;
        } else if (password.equals("q")) {
            System.out.println("\bRegistration cancelled");
            return;
        }

        Customer newCustomer = new Customer(username, password);
        this.customerList.add(newCustomer);
        this.setCurrentCustomer(newCustomer);

        System.out.println("\bRegistation successful! Auto logging in now");
    }

    public void login() {
        this.currentCustomer = null;
        this.currentStaff = null;


        System.out.println("Please enter username and password (q to cancel)");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Username: ");

        String username = "";
        try {
            username = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (username.equals("q")) {
            System.out.println("Login cancelled");
            return;
        }

        System.out.println("Password: ");
        EraserThread et = new EraserThread();
        String password = this.readPassword(et, in);

        if (password.equals("q")) {
            System.out.println("Login cancelled");
            return;
        }

        for (Customer customer : this.customerList) {
            if (customer.login(username, password)) {
                this.setCurrentCustomer(customer);

                System.out.println("\bYou have successfully logged in.");
                System.out.println(String.format("Welcome %s.", username));

                // scanner.close();
                return;
            }
        }

        for (Staff staff : this.staff) {
            if (staff.login(username, password)) {
                this.setCurrentStaff(staff);

                System.out.println("\bYou have successfully logged in");
                if (staff.isManager()) {
                    System.out.println(String.format("Welcome manager %s.", username));
                }
                else {
                    System.out.println(String.format("Welcome staff member %s.", username));
                }
                
                return;
            }
        }

        System.out.println("\bIncorrect login details, please try again or make sure you have registered");
        // scanner.close();
    }

    static class EraserThread implements Runnable {
        private boolean stop;

        public EraserThread() {
        }

        public void run() {
            int priority = Thread.currentThread().getPriority();
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                stop = true;
                while (stop) {
                    System.out.print("\010*");
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            } finally {
                // System.out.println("\b");
                Thread.currentThread().setPriority(priority);
            }
        }

        public void stopMasking() {
            this.stop = false;
        }
    }

    public String readPassword(EraserThread et, BufferedReader in) {
        Thread mask = new Thread(et);
        mask.start();
        // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        et.stopMasking();
        return password;
    }

    public static void main(String[] args) {

        MovieAssingment.Run defaultRun = new MovieAssingment.Run(new ArrayList<>(Arrays.asList(
                new Cinema(Cinema.CinemaType.Bronze, (float) 12, 25, 25, 25, new ArrayList<Session>()),
                new Cinema(Cinema.CinemaType.Silver, (float) 20, 15, 15, 15, new ArrayList<Session>()),
                new Cinema(Cinema.CinemaType.Gold, (float) 40, 15, 15, 15, new ArrayList<Session>())
        )), new ArrayList<>(Arrays.asList(
                new Movie("Harry Potter", Movie.Rating.PG, "You're a wizard Harry", LocalDate.parse("2001-11-29"), "Chris Columbus",
                        new ArrayList<String>(Arrays.asList("Daniel Radcliffe", "Emma Watson", "Rupert Grint"))),
                new Movie("Suicide Squad", Movie.Rating.MA, "Death Squad", LocalDate.parse("2021-08-05"), "James Gunn",
                        new ArrayList<String>(Arrays.asList("Sylvester Stallone", "Jai Courtney", "Viola Davis"))),
                new Movie("Black Widow", Movie.Rating.M, "Natasha Romanoff, a former KGB spy, is shocked to find out...", LocalDate.parse("2021-07-29"), "Cate Shortland",
                        new ArrayList<String>(Arrays.asList("Scarlett Johansson", "David Harbour", "Florence Pugh")))
        )));

        // Create example sessions
        ArrayList<Session> example_sessions = new ArrayList<>(Arrays.asList(
                new Session(LocalDateTime.now(), defaultRun.getMovieList().get(0), defaultRun.getCinemaList().get(0)),
                new Session(LocalDateTime.now().plusDays(1), defaultRun.getMovieList().get(0), defaultRun.getCinemaList().get(0)),
                new Session(LocalDateTime.now().plusDays(1).plusHours(2), defaultRun.getMovieList().get(2), defaultRun.getCinemaList().get(0)),

                new Session(LocalDateTime.now(), defaultRun.getMovieList().get(1), defaultRun.getCinemaList().get(1)),
                new Session(LocalDateTime.now().plusDays(1), defaultRun.getMovieList().get(0), defaultRun.getCinemaList().get(1)),
                new Session(LocalDateTime.now().plusHours(2), defaultRun.getMovieList().get(2), defaultRun.getCinemaList().get(1)),

                new Session(LocalDateTime.now(), defaultRun.getMovieList().get(1), defaultRun.getCinemaList().get(2)),
                new Session(LocalDateTime.now().plusDays(1), defaultRun.getMovieList().get(1), defaultRun.getCinemaList().get(2)),
                new Session(LocalDateTime.now().plusHours(2), defaultRun.getMovieList().get(2), defaultRun.getCinemaList().get(2))
        ));

        defaultRun.parseJSON("./src/main/java/MovieAssingment/credit_cards.json");

        // Remove association to the sessions
        example_sessions = null;

        // Create example staff
        defaultRun.staff = new ArrayList<>(Arrays.asList(
                new Staff("staff", "pass", false),
                new Staff("manager", "pass", true)
        ));

        // Create example customers
        defaultRun.customerList = new ArrayList<>(Arrays.asList(
                new Customer("Archie", "pass"),
                new Customer("Aiden", "pass"),
                new Customer("Jack", "pass"),
                new Customer("Paul", "pass"),
                new Customer("Seb", "pass")
        ));

        // Create example bookings and failed transactions
        defaultRun.bookings = new ArrayList<>(Arrays.asList(
                new Booking("1", defaultRun.getMovieList().get(0).getSessions().get(0), defaultRun.customerList.get(0), true, null, null, LocalDateTime.now()),
                new Booking("2", defaultRun.getMovieList().get(1).getSessions().get(0), defaultRun.customerList.get(1), true, null, null, LocalDateTime.now().minusHours(2)),
                new Booking("4", defaultRun.getMovieList().get(2).getSessions().get(0), defaultRun.customerList.get(2), true, null, null, LocalDateTime.now().minusDays(1)),
                new Booking("5", defaultRun.getMovieList().get(1).getSessions().get(1), defaultRun.customerList.get(3), true, null, null, LocalDateTime.now().minusDays(1).minusHours(5)),
                new Booking("7", defaultRun.getMovieList().get(0).getSessions().get(1), defaultRun.customerList.get(4), true, null, null, LocalDateTime.now().plusHours(2))
        ));

        // Failed transactions examples
        defaultRun.failedTransactions = new ArrayList<>(Arrays.asList(
                new Booking("3", defaultRun.getMovieList().get(0).getSessions().get(0), defaultRun.customerList.get(0), false, "Timeout", null, LocalDateTime.now().minusHours(15)),
                new Booking("6", defaultRun.getMovieList().get(1).getSessions().get(1), defaultRun.customerList.get(1), false, "User cancelled", null, LocalDateTime.now().minusHours(2)),
                new Booking("8", defaultRun.getMovieList().get(2).getSessions().get(2), defaultRun.customerList.get(2), false, "Patment failed", null, LocalDateTime.now().minusDays(1))
        ));

        System.out.println("Welcome to the beta PAJAS movie command line interface.");


        while(true) {
            if(defaultUI(defaultRun) != -1) {
                break;
            }
        }

        return;
    }

    public static  int defaultUI(Run defaultRun) {
        Scanner scan = new Scanner(System.in);
        Integer input_int;
        while (true) {
            System.out.println("Select an option:\n0 - Quit\n1 - Book a Session\n2 - View and Filtered Movie Sessions\n3 - View Movie Information\n4 - Log In\n5 - Register");
            String input = scan.nextLine();

            switch (input) {
                case "0": // Exit
                    scan.close();
                    return -1;

                case "1": //Book a session
                    if (defaultRun.currentCustomer == null) {
                        System.out.println("Please log in");
                    } else {
                        try {
                            boolean s = CompletableFuture.supplyAsync(() -> defaultRun.makeBooking(scan))
                                    .get(120, TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            System.out.println("Timeout has occurred, returning to the home page\n");
                            return -1;
                        } catch (InterruptedException | ExecutionException e) {
                            System.out.println("Booking interupted");
                            return -1;
                        }
                        return -1;
                    }
                    break;

                case "2": //Filter Sessions
                    ArrayList<Session> filterResult = new ArrayList<Session>();
                    ArrayList<Session> sessionsByClass = new ArrayList<Session>();
                    ArrayList<Session> sessionsByTime = new ArrayList<Session>();
                    Boolean filterByClass = true;
                    Boolean filterByTime = true;

                    System.out.println("You can filter by Rating, Cinema Class and Time.");

                    //     case "1": // Filter by Ratings
                    System.out.println("1 - G\n2 - PG\n3 - M\n4 - MA15+\n5 - R18+\nAny other character to skip filter");
                    input = scan.nextLine();
                    switch (input) {
                        case "1": // G
                            for (Movie movie : defaultRun.filterMovieByRating(Rating.G)) {
                                filterResult.addAll(movie.getSessions());
                            }
                            break;
                        case "2": // PG
                            for (Movie movie : defaultRun.filterMovieByRating(Rating.PG)) {
                                filterResult.addAll(movie.getSessions());
                            }
                            break;
                        case "3": // M
                            for (Movie movie : defaultRun.filterMovieByRating(Rating.M)) {
                                filterResult.addAll(movie.getSessions());
                            }
                            break;
                        case "4": // MA15+
                            for (Movie movie : defaultRun.filterMovieByRating(Rating.MA)) {
                                filterResult.addAll(movie.getSessions());
                            }
                            break;
                        case "5": // R18+
                            for (Movie movie : defaultRun.filterMovieByRating(Rating.R)) {
                                filterResult.addAll(movie.getSessions());
                            }
                            break;
                        default:
                            for (Movie movie : defaultRun.movieList) {
                                filterResult.addAll(movie.getSessions());
                            }
                    }

                    // case "2": // Filter by Cinema Class
                    System.out.println("1 - Gold\n2 - Silver\n3 - Bronze\nAny other character to skip filter");
                    input = scan.nextLine();
                    switch (input) {
                        case "1": // Gold
                            sessionsByClass = defaultRun.filterSessionByCinemaClass(CinemaType.Gold);
                            break;
                        case "2": // Silver
                            sessionsByClass = defaultRun.filterSessionByCinemaClass(CinemaType.Silver);
                            break;
                        case "3": // Bronze
                            sessionsByClass = defaultRun.filterSessionByCinemaClass(CinemaType.Bronze);
                            break;
                        default:
                            filterByClass = false;
                    }
                    if (filterByClass == true) {
                        //get the intersection of sessionsByClass and filterResults
                        filterResult.retainAll(sessionsByClass);
                    }

                    // case "3": // Filter by Time
                    while (true) {
                        System.out.println("Please enter a number from 1-7 (Monday to Sunday) or the character n to skip filter");
                        input = scan.nextLine();
                        if (input.equals("n") || input.equals("")) {
                            filterByTime = false;
                            break;
                        } else {
                            try {
                                input_int = Integer.parseInt(input);
                                if (input_int > 7 || input_int < 1) {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                System.out.println("Please enter valid input");
                                continue;
                            }
                            sessionsByTime = defaultRun.filterSessionByTime(defaultRun.getMovieList(), input_int);
                            break;
                        }
                    }
                    if (filterByTime) {
                        // get the intersection of filterResults and filterbytime list
                        filterResult.retainAll(sessionsByTime);
                    }

                    System.out.println("Please select a movie to display sessions for");

                    for (int i = 0; i < defaultRun.getMovieList().size(); i++) {
                        System.out.println((i + 1) + " - " + defaultRun.getMovieList().get(i).getTitle());
                    }
                    System.out.println((defaultRun.getMovieList().size()+1) + " - Skip filter");
                    while (true) {
                        input = scan.nextLine();

                        try {
                            input_int = Integer.parseInt(input);
                            if (input_int < 1 || input_int > (defaultRun.getMovieList().size()+1)) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid selection ... must be integer from  1 - " + defaultRun.getMovieList().size());
                        }
                    }

                    if(input_int != defaultRun.getMovieList().size()+1) {
                        ArrayList<Session> movieFiltered = new ArrayList<Session>();
                        for(int i = 0; i < filterResult.size(); i++) {
                            if(filterResult.get(i).getMovie().getTitle().equals(defaultRun.movieList.get(input_int - 1).getTitle())) {
                                movieFiltered.add(filterResult.get(i));
                            }
                        }
                        filterResult = movieFiltered;
                    }


                    if (filterResult.size() > 0) {
                        System.out.println(defaultRun.displaySessionsList(filterResult));
                    } else {
                        System.out.println("There are no sessions which match your filters :(\n\n");
                    }
                    break;

                case "3": //View Movie Info
                    System.out.println("Please select a movie to display information for:");
                    for (int i = 0; i < defaultRun.getMovieList().size(); i++) {
                        System.out.println((i + 1) + " - " + defaultRun.getMovieList().get(i).getTitle());
                    }
                    while (true) {
                        input = scan.nextLine();

                        try {
                            input_int = Integer.parseInt(input);
                            if (input_int < 1 || input_int > defaultRun.getMovieList().size()) {
                                throw new Exception();
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid selection ... must be integer");
                        }
                    }
                    System.out.println(defaultRun.viewMovieInfo(defaultRun.movieList.get(input_int - 1)));
                    System.out.print("Sessions: \n    ");
                    System.out.println(defaultRun.displayMovieSessions(defaultRun.movieList.get(input_int - 1)));

                    break;

                case "4": //Log In
                    Customer customer = new Customer("test", "password");
                    Staff staff = new Staff("staff01", "password", false);
                    Staff manager = new Staff("manager01", "Securepassword", true);
                    defaultRun.addCustomer(customer);
                    defaultRun.addStaffMember(staff);
                    defaultRun.addStaffMember(manager);
                    defaultRun.login();
                    if (defaultRun.currentStaff != null) {
                        // System.out.println("Welcome staff member.");
                        // scan.close();
                        staffUI(defaultRun, scan);
                    } else if (defaultRun.currentCustomer != null) {
                        // Succesful customer login
                        // System.out.println("Successfully logged in\nWelcome valued customer");
                    } else {
                        // Unsuccessful login
                    }
                    break;

                case "5": //Register
                    defaultRun.registration();
                    break;

                default:
//                    System.out.println("Invalid selection");
                    break;

            }
        }
    }

    public static void staffUI(Run defaultRun, Scanner scan) {
        while(true) {
            System.out.println(
                    "Select an option:\n" +
                            "1 - Add a Movie\n" +
                            "2 - Modify a Movie\n" +
                            "3 - Delete a Movie\n4 - Add a Session\n" +
                            "5 - Get staff report 1:\n\tList of upcoming movies with item details\n" +
                            "6 - Get staff report 2:\n\tSummary of bookings for each movie session with details\n" +
                            "7 - Add GiftCard\n" +
                            "8 - Logout\n");
            if (defaultRun.currentStaff != null && defaultRun.currentStaff.isManager()) {
                System.out.println("Manager functions:\n" +
                        "9 - Add a staff member\n" +
                        "10 - Remove a staff member\n" +
                        "11 - View failed transactions\n"
                );
            }
            String input = scan.nextLine();
            switch(input) {
                case "1":
                    Movie newMovie = defaultRun.staffAddEditMovie(null);
                    if (newMovie != null) {
                        defaultRun.addMovie(newMovie);
                        System.out.print("New movie added:");
                        System.out.println(defaultRun.viewMovieInfo(newMovie));
                    } else {
                        System.out.println("Failed to add new movie, please try again.");
                    }
                    break;
                case "2":
                    System.out.println("Which movie would you like to edit? (q to cancel)");
                    // Figure out which movie to edit
                    Movie input_movie = getMovieSelection(scan, defaultRun.getMovieList());

                    Movie ret = defaultRun.staffAddEditMovie(input_movie);
                    if (ret != null) {
                        System.out.print("Information updated to:");
                        System.out.println(defaultRun.viewMovieInfo(input_movie));
                    }
                    break;

                case "3":
                    Movie movieToDelete = getMovieSelection(scan, defaultRun.getMovieList());

                    System.out.format("Are you sure you wish to delete %s?\n And by extension all movie sessions?\n1 - Yes\nAny other character to abort\n", movieToDelete.getTitle());

                    String confirm_input = scan.nextLine();

                    switch(confirm_input) {
                        case "1":
                            movieToDelete.safeDeleteSessions();
                            defaultRun.movieList.remove(0);
                            System.out.println(defaultRun.getMovieList());
                            break;
                        default:
                            System.out.println("Aborting");
                            break;
                    }
                    break;
                case "4":
                    Session newSession = staffAddSession(defaultRun);
                    if (newSession != null) {
                        System.out.println("New session successfully added...\n");
                    } else {
                        System.out.println("Something went wrong while making the session, please try again.\n");
                    }
                    break;

                case "5":
                    String report1 = defaultRun.getUpcomingSessions(defaultRun);
                    System.out.println(report1);
                    writeToFile(report1, "src/staff_reports/staff_report_upcoming_sessions");
                    System.out.println("System report printed to file path: src/staff_reports/staff_report_upcoming_sessions");
                    break;

                case "6":
                    String report2 = defaultRun.getSessionsSummary(defaultRun);
                    System.out.println(report2);
                    writeToFile(report2, "src/staff_reports/staff_report_session_summary");
                    System.out.println("System report printed to file path: src/staff_reports/staff_report_session_summary");
                    break;

                case "7":
                    staffAddGiftCard(defaultRun);
                    break;

                case "8":
                    System.out.println("Logging out");
                    defaultRun.currentStaff = null;
                    return;
            }

            if (defaultRun.currentStaff != null && defaultRun.currentStaff.isManager()) {
                switch (input) {
                    case "9":
                        defaultRun.managerAddStaffMember();
                        break;

                    case "10":
                        defaultRun.managerRemoveStaffMember();
                        break;

                    case "11":
                        String output = defaultRun.getFailedTransactions();
                        System.out.println(output);
                        // Print to file
                        writeToFile(output, "src/staff_reports/manager_failedTransactions_summary");
                        break;
                }
            }
        }
    }

    public static void writeToFile(String report, String reportName) {
        String fileName = String.format("%s.txt", reportName);

        try {
            File myObj = new File(fileName);
            if (!myObj.createNewFile()) {
                System.out.println("File already exists. It has been updated");
            }
            Files.write(Paths.get(fileName), report.getBytes());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

