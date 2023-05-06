package mytaxi.krutyporokh.models;

public enum PaymentType {
    CASH("CASH"),
    CARD("CARD");

    private final String paymentType;


    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType(){
        return paymentType;
    }
}
