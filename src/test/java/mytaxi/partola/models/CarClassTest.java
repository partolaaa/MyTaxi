package mytaxi.partola.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivan Partola
 * @date 14.05.2023
 */
class CarClassTest {

    @Test
    void show() {
        assertEquals("Business", CarClass.BUSINESS.show());
        assertEquals("Economy", CarClass.ECONOMY.show());
    }
}