package mytaxi.krutyporokh.models;

public enum OrderStatus {
    NOT_ACCEPTED("NOT_ACCEPTED"),
    ACCEPTED("ACCEPTED"),
    WAITING_FOR_CLIENT("WAITING_FOR_CLIENT"),
    IN_PROCESS("IN_PROCESS"),
    COMPLETED("COMPLETED");
    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getValue(){
        return orderStatus;
    }

    public String show () {
        // NOT_ACCEPTED -> Not accepted
        String temp = orderStatus
                .replaceAll("_", " ")
                .toLowerCase();
        temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);

        return temp;
    }
}