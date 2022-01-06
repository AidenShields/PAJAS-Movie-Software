package MovieAssingment;

public class GiftCard {
    private String giftCardNumber;
    private boolean redeemed;

    public GiftCard(String giftCardNumber) {
        this.giftCardNumber = giftCardNumber;
        this.redeemed = false;
    }

    public String getGiftCardNumber() {
        return giftCardNumber;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void redeem() {
        this.redeemed = true;
    }
}
