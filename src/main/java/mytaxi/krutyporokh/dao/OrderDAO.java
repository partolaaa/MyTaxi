package mytaxi.krutyporokh.dao;

import mytaxi.krutyporokh.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDAO {

    private JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createNewOrder(Order order) {
        String query = "INSERT INTO \"Order\" (client_id, driver_id, booking_datetime, pickup_address, destination_address, passenger_name, passenger_phone_number, booking_notes, payment_type, pay_with_bonuses, order_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::payment_type, ?, ?::order_status)";

        jdbcTemplate.update(query,
                order.getClientId(),
                order.getDriverId(),
                order.getBookingDateTime(),
                order.getPickupAddress(),
                order.getDestinationAddress(),
                order.getPassengerName(),
                order.getPassengerPhoneNumber(),
                order.getBookingNotes(),
                order.getPaymentType().toString(),
                order.isPayWithBonuses(),
                order.getOrderStatus().toString()
        );
    }

}
