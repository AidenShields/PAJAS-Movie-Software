package MovieAssingment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RunTest {
    static Run defaultRun;


    @BeforeAll
    static void beforeTests() {
        defaultRun = new Run(new ArrayList<>(Arrays.asList(
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
                new Booking("3", defaultRun.getMovieList().get(0).getSessions().get(0), defaultRun.customerList.get(0), false, null, null, LocalDateTime.now().minusHours(15)),
                new Booking("6", defaultRun.getMovieList().get(1).getSessions().get(1), defaultRun.customerList.get(1), false, null, null, LocalDateTime.now().minusHours(2)),
                new Booking("8", defaultRun.getMovieList().get(2).getSessions().get(2), defaultRun.customerList.get(2), false, null, null, LocalDateTime.now().minusDays(1))
        ));
    }

    @Test
    public void filterMovieByRatingTest(){
        Movie mov1 = defaultRun.getMovieList().get(0);
        Movie mov2 = defaultRun.getMovieList().get(1);

        ArrayList<Movie> movs_rating_1 = defaultRun.filterMovieByRating(mov1.getRating());
        ArrayList<Movie> movs_rating_2 = defaultRun.filterMovieByRating(mov2.getRating());

        assertEquals(mov1, movs_rating_1.get(0), "Test to filter by rating failed");
        assertEquals(mov2, movs_rating_2.get(0), "Test to filter by rating failed");
    }

    @Test
    public void filterSessionByCinemaClassTest(){
        System.out.println("Testing the filter Sessions by time Cinema Class");

        LocalDate today = LocalDate.now();
        int count = 0;
        for (Session sesh: defaultRun.filterSessionByCinemaClass(Cinema.CinemaType.Silver)) {
            assertEquals(Cinema.CinemaType.Silver, sesh.getCinema().getCinemaType(), "Filter by cinema class produced an inccorect output");
            count++;
        }
        System.out.println("The filter produced " + count + " sessions in the silver class Cinema");
        assertEquals(3, count, "The number of sessions in that cinema class has a miss match");
    }

//    @Test
//     public void filterSessionByTimeTest() {
//         System.out.println("Testing the filter Movie by time function");

//         LocalDate today = LocalDate.now();
//         int count = 0;
//         for (Session sesh: defaultRun.filterSessionByTime(defaultRun.getMovieList(), 2)) {
//             assertEquals(today, sesh.getSessionTime().toLocalDate(), "FilterMovieByTime produced an inccorect output");
//             count++;
//         }
//         System.out.println("The filter produced " + count + " sessions for the specified day");
//         assertEquals(5, count, "Testing the number of example sessions are today failed");
//     }

    @Test public void displaySessionsListTest() {
        System.out.println("Testing displaySessions function and the correct output");
        Cinema cin1 = defaultRun.getCinemaList().get(0);
        String exampleOutput = defaultRun.displaySessionsList(cin1.getSessions());
        System.out.println(exampleOutput);

        for (Session sesh: cin1.getSessions()) {
            assertTrue(exampleOutput.contains(sesh.getMovie().getTitle()),
                    "The display sessions function missed one of the provided sessions");
        }
    }

    @Test public void displayMovieSessionsTest() {
        System.out.println("Testing displayMovieSessions function and occurances of the session");
        //assertEquals(defaultRun.displayMovieSessions(defaultRun.movieList.get(0)), true);
        Movie exampleMovie = defaultRun.getMovieList().get(0);
        String exampleOutput = defaultRun.displayMovieSessions(exampleMovie);
        System.out.println(exampleOutput);

        assertEquals(exampleMovie.getSessions().size(),
                exampleOutput.split("\n").length-1,
                "The display movie sessions did not print out all available sessions");
    }

    @Test public void displayAllSessions() {
        System.out.println("Testing the display all sessions function");
        String output = defaultRun.displayAllSessions();
        assertTrue(output.length() > 0);
        System.out.println(output);
    }

    @Test
    public void viewMovieInfoTest() {
        System.out.println("View movie information test");
        Movie mov = defaultRun.getMovieList().get(0);
        String output = defaultRun.viewMovieInfo(mov);
        System.out.println(output);

        assertTrue(output.contains(mov.getTitle()), "View Movie Info does not show title");
        assertTrue(output.contains(mov.getRating().name()), "View Movie Info does not show rating");
        assertTrue(output.contains(mov.getDirector()), "View Movie Info does not show director");
    }

    @Test
    public void paymentCreditCardStandardSuccessNoSave() throws Exception{
        System.out.println("Standard test for payment credit card success with no saved credit card nor saving credit card");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        CreditCard cc = new CreditCard("Messi", "11111");
        run.addCreditCard(cc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Messi\n11111\nn";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertTrue(result);
    }

    @Test
    public void paymentCreditCardStandardSuccessSave() throws Exception{
        System.out.println("Standard test for payment credit card success with no saved credit card and saving entered card with customer");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        CreditCard cc = new CreditCard("Messi", "11111");
        run.addCreditCard(cc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Messi\n11111\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertTrue(result);

        CreditCard testCC = run.currentCustomer.getSavedCreditCard();
        // Test for correct saving of credit card details for customer
        assertEquals(cc, testCC);
    }

    @Test
    public void paymentCreditCardMistakeLoopSuccess() throws Exception{
        System.out.println("Test for case when user inserts wrong details, if the system can correctly loop and return correct value");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        CreditCard cc = new CreditCard("Messi", "11111");
        run.addCreditCard(cc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Messi\n1111\nMessi\n11111\nn";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertTrue(result);
    }

    @Test
    public void paymentCreditCardCancelTransaction() throws Exception{
        System.out.println("Test for correct return boolean when user cancels transaction");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "q";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertFalse(result);
    }

    @Test
    public void paymentCreditCardSavedCreditCard() throws Exception{
        System.out.println("Test for correct return boolean when user has a saved credit card");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        CreditCard cc = new CreditCard("Messi", "11111");
        run.addCreditCard(cc);
        run.currentCustomer.addSavedCreditCard(cc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        InputStream is = System.in;
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertTrue(result);
    }

    @Test
    public void paymentCreditCardCancelTransactionFailedAttempt() throws Exception{
        System.out.println("Test for successful exit and return value after user attempts to exit after wrong input");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        CreditCard cc = new CreditCard("Messi", "11111");
        run.addCreditCard(cc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));
        
        String inputStream = "Messi\n12345\nq";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentCreditCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertFalse(result);
    }

    @Test
    public void paymentGiftCardSuccess() throws Exception{
        System.out.println("Standard test for payment by gift card success and test if correct status update for gift card");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        GiftCard gc = new GiftCard("GC1111222233334444");
        run.addGiftCard(gc);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "GC1111222233334444";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentGiftCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertTrue(result);

        // Test for correct gift card redeem status
        assertTrue(gc.isRedeemed());
    }

    @Test
    public void paymentGiftCardFailureNotExist() throws Exception{
        System.out.println("Standard test for payment by gift card failure doesn't exist");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        // GiftCard gc = new GiftCard();

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "GC1111222233334444\nq";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentGiftCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertFalse(result);
    }

    @Test
    public void paymentGiftCardFailureRedeemed() throws Exception{
        System.out.println("Standard test for payment by gift card failure already redeemed");
        
        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Messi", "password123");
        run.setCurrentCustomer(customer);
        GiftCard gc = new GiftCard("GC1111222233334444");
        gc.redeem();
        run.addGiftCard(gc);
        

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "GC1111222233334444\nq";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        Scanner scanner = new Scanner(System.in);

        boolean result = false;

        try{
            result = run.paymentGiftCard();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        // Test for correct return boolean
        assertFalse(result);
    }


    @Test
    public void loginTestCustomer() throws Exception{
        System.out.println("Test for successful login by customer");

        // initialising variables   
        Run run = new Run();
        Customer customer = new Customer("customer", "password");
        run.addCustomer(customer);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "customer\npassword";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);

        try{
            run.login();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentStaff() == null);
        assertTrue(run.getCurrentCustomer() != null);        
    }

    @Test
    public void loginTestStaff() throws Exception{
        System.out.println("Test for successful login by general staff member");

        // initialising variables
        Run run = new Run();
        Staff staff = new Staff("Staff01", "password", false);
        run.addStaffMember(staff);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Staff01\npassword";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.login();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentCustomer() == null);
        assertTrue(run.getCurrentStaff() != null);  
    }

    @Test
    public void loginTestManager() throws Exception{
        System.out.println("Test for successful login by manager staff member");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("BigBoss", "Securepassword", false);
        run.addStaffMember(manager);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "BigBoss\nSecurepassword";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.login();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentCustomer() == null);
        assertTrue(run.getCurrentStaff() != null); 
    }

    @Test
    public void loginCancelTest() throws Exception{
        System.out.println("Test for successful exit of login process with input \'q\'");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("BigBoss", "Securepassword", false);
        run.addStaffMember(manager);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "BigBoss\nq";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.login();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentCustomer() == null);
        assertTrue(run.getCurrentStaff() == null); 
    }

    @Test
    public void loginFailedTest() throws Exception{
        System.out.println("Test for failure when password does not match the user's password");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("customer01", "Securepassword");
        run.addCustomer(customer);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "customer01\nhacked";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.login();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentCustomer() == null);
        assertTrue(run.getCurrentStaff() == null); 
    }


    @Test
    public void registrationCustomerTest() throws Exception{
        System.out.println("Test for successful registration of customer account");

        // initalising variables
        Run run = new Run();


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "newCustomer\npassword";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.registration();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentStaff() == null); 
        assertTrue(run.getCurrentCustomer() != null);
    }

    @Test
    public void registrationCustomerFailedCustomerTest() throws Exception{
        System.out.println("Test for registration failure when username already exists in the system (customer account)");


        // initalising variables
        Run run = new Run();
        Customer customer = new Customer("Molchat", "Doma");
        run.addCustomer(customer);

        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Molchat\nPassword";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.registration();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentStaff() == null); 
        assertTrue(run.getCurrentCustomer() == null);
    }
    
    @Test
    public void registrationCustomerFailedStaffTest() throws Exception{
        System.out.println("Test for registration failure when username already exists in the system (staff account)");

        // initalising variables
        Run run = new Run();
        Staff staff = new Staff("Dreamland", "GlassAnimals", false);
        run.addStaffMember(staff);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Dreamland\nGlassAnimals";
        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        
        Scanner scanner = new Scanner(System.in);


        try{
            run.registration();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        assertTrue(run.getCurrentStaff() == null); 
        assertTrue(run.getCurrentCustomer() == null);
    }

    @Test
    public void managerAddStaffMemberTestSuccess() throws Exception{
        System.out.println("Standard test for successful addition of staff member by manager");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        run.addStaffMember(manager);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "lolcat\npassword\nn";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerAddStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("lolcat", "password")){
                exists = true;
            }
        }

        assertTrue(exists);
    }

    @Test
    public void ManagerAddManagerStaffMember() throws Exception{
        System.out.println("Standard test for successful addition of manager staff member by manager");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        run.addStaffMember(manager);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "babyboss\ngreatmovie\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerAddStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("babyboss", "greatmovie") && s.isManager()){
                exists = true;
            }
        }

        assertTrue(exists);
    }

    @Test
    public void ManagerAddStaffMemberCancelled() throws Exception{
        System.out.println("Standard test for exiting the creation of another staff member using input \'q\'");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        run.addStaffMember(manager);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "babyboss\nlol\nq";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerAddStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("babyboss", "greatmovie") && s.isManager()){
                exists = true;
            }
        }

        assertFalse(exists);
    }

    @Test
    public void ManagerAddStaffMemberFailed() throws Exception{
        System.out.println("Standard test for failure to add new staff member as an account with the username already exists");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        Staff staff = new Staff("A$AP", "rocky", false);
        run.addStaffMember(manager);
        run.setCurrentStaff(manager);
        run.addStaffMember(staff);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "A$AP\nrocky\nn";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerAddStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        if(run.getStaffMembers().size() > 2){
            exists = true;
        }

        assertFalse(exists);
    }

    @Test
    public void ManagerRemoveStaffMemberTest() throws Exception{
        System.out.println("Standard test for successful removal of staff member by manager");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        Staff staff = new Staff("bob", "thebuilder", false);
        run.addStaffMember(manager);
        run.addStaffMember(staff);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "bob\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerRemoveStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("bob", "thebuilder")){
                exists = true;
            }
        }

        assertFalse(exists);
    }
    

    @Test
    public void ManagerRemoveStaffCancelledTest() throws Exception{
        System.out.println("Standard test for successful removal of manager staff member by manager");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        Staff staff = new Staff("bob", "thebuilder", false);
        run.addStaffMember(manager);
        run.addStaffMember(staff);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "bob\nn";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerRemoveStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("bob", "thebuilder")){
                exists = true;
            }
        }

        assertTrue(exists);
    }    
    
    public void ManagerRemoveStaffExitTest() throws Exception{
        System.out.println("Standard test for exiting removal of staff member process by input \'q\'");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        Staff staff = new Staff("bob", "thebuilder", false);
        run.addStaffMember(manager);
        run.addStaffMember(staff);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "q";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerRemoveStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("bob", "thebuilder")){
                exists = true;
            }
        }

        assertTrue(exists);
    }

    public void ManagerRemoveStaffTestFailed() throws Exception{
        System.out.println("Standard test for failure when trying to remove the current operating account");

        // initialising variables
        Run run = new Run();
        Staff manager = new Staff("Master", "Securepassword", true);
        run.addStaffMember(manager);
        run.setCurrentStaff(manager);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Master\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerRemoveStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        if (run.getStaffMembers().size()>0){
            exists = true;
        }

        assertTrue(exists);
    }

    public void ManagerAddStaffMemberFailureAccessLevel() throws Exception{
        System.out.println("Standard test for failure when trying to access staff member adding with insufficient access privilege");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Doctor", "Dre");
        run.addCustomer(customer);
        run.setCurrentCustomer(customer);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "HackerMan\nultrasecurepassword\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerAddStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("HackerMan", "ultrasecurepassword")){
                exists = true;
            }
        }

        assertFalse(exists);
    }

    public void ManagerRemoveStaffMemberFailureAccessLevel() throws Exception{
        System.out.println("Standard test for failure when trying to access staff member removal with insufficient access privilege");

        // initialising variables
        Run run = new Run();
        Customer customer = new Customer("Doctor", "Dre");
        Staff staff = new Staff("Mario", "Luigi", false);
        run.addCustomer(customer);
        run.addStaffMember(staff);
        run.setCurrentCustomer(customer);


        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));

        String inputStream = "Mario\ny";

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));

        
        Scanner scanner = new Scanner(System.in);


        try{
            run.managerRemoveStaffMember();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        System.setOut(orig);

        // Print the system
        String actual = os.toString("UTF-8");

        // System.out.println(actual);

        boolean exists = false;
        // Test for correct return boolean
        for (Staff s : run.getStaffMembers()){
            if (s.login("Mario", "Luigi")){
                exists = true;
            }
        }

        assertTrue(exists);
    }

    @Test
    public void systemAddStaffMemberTest(){
        // initialising variables    
        Run run = new Run();
        Staff staff = new Staff("dropout", "brakence", false);
        run.addStaffMember(staff);

        assertEquals(staff, run.getStaffMembers().get(0));
    }

    @Test
    public void systemRemoveStaffMemberTest(){
        // initialising variables
        Run run = new Run();
        Staff staff = new Staff("Spy?", "overcast", false);
        run.addStaffMember(staff);

        run.removeStaffMemeber(staff);

        assertEquals(0, run.getStaffMembers().size());
    }

    @Test
    public void getUpcomingSessionsTest() {
        // Run command here
        try {
            defaultRun.getUpcomingSessions(defaultRun);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check file was generated
        File f = new File("src/staff_reports/staff_report_upcoming_sessions.txt");
        assertTrue(f.exists() && !f.isDirectory(), "Did not generate staff upcoming sessions file");

    }

    @Test
    public void getSessionsSummaryTest() {
        // Run command here
        try {
            defaultRun.getSessionsSummary(defaultRun);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check file was generated
        File f = new File("src/staff_reports/staff_report_session_summary.txt");
        assertTrue(f.exists() && !f.isDirectory(), "Did not generate staff session summary file");
    }

    @Test
    public void getfailedTransactionsTest() {
        Staff testStaff = new Staff("test", "test", true);
        PrintStream origOut = System.out;
        InputStream origIn = System.in;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(os, false, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inputStream = "1\n01/01 10:10\n11/04 12:00\n1\ny\n";

        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        defaultRun.setCurrentStaff(testStaff);
        // Run command here
        try {
            defaultRun.getFailedTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        defaultRun.setCurrentStaff(null);

        System.setIn(origIn);
        System.setOut(origOut);

        // Check file was generated
        File f = new File("src/staff_reports/manager_failedTransactions_summary.txt");
        assertTrue(f.exists() && !f.isDirectory(), "Did not generate manager failed transactions file");
}

    @Test
    public void staffAddMovieTest() {
        Movie testMovie = null;
        PrintStream origOut = System.out;
        InputStream origIn = System.in;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(os, false, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inputStream = "Test Movie\ng\nTest synopsis\n01/01/2000\nTester man\nman1,man2\ny\n";

        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        // Run command here
        try {
            testMovie = defaultRun.staffAddEditMovie(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(origIn);
        System.setOut(origOut);

        assertEquals("Test Movie", testMovie.getTitle(), "New movie didn't set correct title");
        assertEquals(Movie.Rating.G, testMovie.getRating(), "New movie didn't set correct rating");
        assertEquals("Test synopsis", testMovie.getSynopsis(), "New movie didn't set correct synopsis");
        assertEquals("Tester man", testMovie.getDirector(), "New movie didn't set correct director");
    }

    @Test
    public void staffEditMovieTest() {
        Movie testMovie = defaultRun.getMovieList().get(0);
        PrintStream origOut = System.out;
        InputStream origIn = System.in;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(os, false, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inputStream = "*2\nM\n*1\nHarry Potter The Editing\n*4\n3\n01/01/2000\ny\n";

        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        // Run command here
        try {
            testMovie = defaultRun.staffAddEditMovie(testMovie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(origIn);
        System.setOut(origOut);

        assertEquals("Harry Potter The Editing", testMovie.getTitle(), "Edit movie didn't set correct title");
        assertEquals(Movie.Rating.M, testMovie.getRating(), "Edit movie didn't set correct rating");
    }

    @Test
    public void staffAddSessionTest() {
        Session newSesh = null;
        PrintStream origOut = System.out;
        InputStream origIn = System.in;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(os, false, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inputStream = "1\n01/01 10:10\n11/04 12:00\n1\ny\n";

        System.setIn(new ByteArrayInputStream(inputStream.getBytes()));
        // Run command here
        try {
            newSesh = defaultRun.staffAddSession(defaultRun);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setIn(origIn);
        System.setOut(origOut);
        System.out.println(newSesh.getCinema().getCinemaType());

        assertEquals("Harry Potter", newSesh.getMovie().getTitle(), "New added session didn't pick correct movie");
        assertEquals(Cinema.CinemaType.Bronze, newSesh.getCinema().getCinemaType(), "New added session didn't pick correct movie");
    }
}