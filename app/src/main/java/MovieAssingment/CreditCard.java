package MovieAssingment;

public class CreditCard {
    private String cardHolderName;
    private String number;

    public CreditCard(String cardHolderName, String number) {
        this.cardHolderName = cardHolderName;
        this.number = number;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
