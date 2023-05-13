package mytaxi.partola.models;

public enum PaymentType {
    CASH("CASH"),
    CARD("CARD");

    private final String paymentType;


    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getValue(){
        return paymentType;
    }
}
