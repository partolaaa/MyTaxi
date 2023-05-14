package mytaxi.krutyporokh.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivan Partola
 * @date 14.05.2023
 */
class OrderStatusTest {

    @Test
    void getValue() {
        assertEquals("NOT_ACCEPTED", OrderStatus.NOT_ACCEPTED.getValue());
        assertEquals("WAITING_FOR_CLIENT", OrderStatus.WAITING_FOR_CLIENT.getValue());
        assertEquals("COMPLETED", OrderStatus.COMPLETED.getValue());
    }

    @Test
    void show() {
        assertEquals("Not accepted", OrderStatus.NOT_ACCEPTED.show());
        assertEquals("Waiting for client", OrderStatus.WAITING_FOR_CLIENT.show());
        assertEquals("Completed", OrderStatus.COMPLETED.show());
    }
}