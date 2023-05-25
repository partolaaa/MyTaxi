package mytaxi.krutyporokh.models;

public enum OrderStatus {
    NOT_ACCEPTED("NOT_ACCEPTED"),
    ACCEPTED("ACCEPTED"),

    CANCELLED("CANCELLED"),
    WAITING_FOR_CLIENT("WAITING_FOR_CLIENT"),
    IN_PROCESS("IN_PROCESS"),
    COMPLETED("COMPLETED"),
    RATED_BY_DRIVER("RATED_BY_DRIVER"),
    RATED_BY_CLIENT("RATED_BY_CLIENT"),
    RATED_BY_ALL("RATED_BY_ALL");
    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getValue(){
        return orderStatus;
    }

    public String show() {
        // NOT_ACCEPTED -> Not accepted
        String temp = orderStatus
                .replaceAll("_", " ")
                .toLowerCase();
        temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);

        return temp;
    }

    public OrderStatus next() {
        OrderStatus nextStatus = null;

        switch (OrderStatus.valueOf(orderStatus)) {
            case ACCEPTED -> nextStatus = WAITING_FOR_CLIENT;
            case WAITING_FOR_CLIENT -> nextStatus = IN_PROCESS;
            case IN_PROCESS -> nextStatus = COMPLETED;
        }

        assert nextStatus != null;
        return nextStatus;
    }
}