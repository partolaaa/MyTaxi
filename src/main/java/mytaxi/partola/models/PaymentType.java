package mytaxi.partola.models;

import java.util.Locale;

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

    public String show () {
        // CASH -> Cash
        return paymentType
                .substring(0, 1).toUpperCase()
                + paymentType.substring(1).toLowerCase();

    }
}
