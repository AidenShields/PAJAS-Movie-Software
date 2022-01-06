package MovieAssingment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {
    static CreditCard card1;
    static CreditCard card2;
    @BeforeAll
    static void beforeTests() {
        card1 = new CreditCard("Jon", "1738");
        card2 = new CreditCard("Jones", "pp poo poo");
    }
    @Test
    void getCardHolderNameTest() {
        System.out.println("Excpeted: Jon");
        System.out.println("Actual: " + card1.getCardHolderName());
        assertEquals(card1.getCardHolderName(), "Jon");

        System.out.println("Excpeted: Jones");
        System.out.println("Actual: " + card2.getCardHolderName());
        assertEquals(card2.getCardHolderName(), "Jones");
    }

    @Test
    void setCardHolderName() {
        System.out.println("Excpeted: Jose");
        String backup1 = card1.getCardHolderName();
        card1.setCardHolderName("Jose");
        System.out.println("Actual: " + card1.getCardHolderName());
        assertEquals(card1.getCardHolderName(), "Jose");
        card1.setCardHolderName(backup1);

        System.out.println("Excpeted: Aldo");
        String backup2 = card2.getCardHolderName();
        card2.setCardHolderName("Aldo");
        System.out.println("Actual: " + card2.getCardHolderName());
        assertEquals(card2.getCardHolderName(), "Aldo");
        card2.setCardHolderName(backup2);
    }

    @Test
    void getNumber() {
        System.out.println("Excpeted: 1738");
        System.out.println("Actual: " + card1.getNumber());
        assertEquals(card1.getNumber(), "1738");

        System.out.println("Excpeted: pp poo poo");
        System.out.println("Actual: " + card2.getNumber());
        assertEquals(card2.getNumber(), "pp poo poo");
    }

    @Test
    void setNumber() {
        System.out.println("Excpeted: 1234");
        String backup1 = card1.getNumber();
        card1.setNumber("1234");
        System.out.println("Actual: " + card1.getNumber());
        assertEquals(card1.getNumber(), "1234");
        card1.setNumber(backup1);

        System.out.println("Excpeted: 1032654984321");
        String backup2 = card2.getNumber();
        card2.setNumber("1032654984321");
        System.out.println("Actual: " + card2.getNumber());
        assertEquals(card2.getNumber(), "1032654984321");
        card2.setNumber(backup2);
    }
}