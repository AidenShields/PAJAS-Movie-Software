package MovieAssingment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiftCardTest {
    static GiftCard card;
    @BeforeAll
    static void beforeTests(){
        card = new GiftCard("GC0011001100110011");
    }
    @Test
    void getGiftCardNumber() {
        assertEquals(card.getGiftCardNumber(), "GC0011001100110011");
    }

    @Test
    void isRedeemed() {
        assertEquals(card.isRedeemed(), false);
    }

    @Test
    void redeem() {
        card.redeem();
        assertEquals(card.isRedeemed(), true);
    }
}