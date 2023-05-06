package mytaxi.krutyporokh.models;

public enum OrderStatus {
    NOT_ACCEPTED("NOT_ACCEPTED"),
    WAITING_FOR_CLIENT("WAITING_FOR_CLIENT"),
    IN_PROCESS("IN_PROCESS"),
    COMPLETED("COMPLETED");
    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus(){
        return orderStatus;
    }
}
