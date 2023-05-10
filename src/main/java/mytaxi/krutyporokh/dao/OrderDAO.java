package mytaxi.krutyporokh.dao;

import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDAO {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createNewOrder(Order order, CustomUser customUser) {
        String query = "INSERT INTO \"Order\" " +
                "(client_id, booking_datetime, pickup_address, destination_address, passenger_name, passenger_phone_number, booking_notes, payment_type, pay_with_bonuses) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::payment_type, ?)";

        jdbcTemplate.update(query,
                customUser.getUserId(),
                order.getBookingDateTime(),
                order.getPickupAddress(),
                order.getDestinationAddress(),
                order.getPassengerName(),
                order.getPassengerPhoneNumber(),
                order.getBookingNotes(),
                order.getPaymentType().toString(),
                order.isPayWithBonuses()
        );
    }

}
