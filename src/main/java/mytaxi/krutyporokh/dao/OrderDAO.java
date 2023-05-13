package mytaxi.krutyporokh.dao;

import mytaxi.krutyporokh.models.Order;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate, UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Transactional
    public void createNewOrder(Order order, Client client) {
        String query = "INSERT INTO \"Order\" " +
                "(client_id, booking_datetime, pickup_address, destination_address, journey_distance, passenger_name, " +
                "passenger_phone_number, booking_notes, payment_type, car_class, vehicle_type, pay_with_bonuses, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::payment_type, ?::car_class, ?::vehicle_type, ?, ?)";

        LocalDateTime dateTime = LocalDateTime.parse(order.getBookingDatetime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Ser datetime to a prettier format
        order.setBookingDatetime(dateTime.format(formatter));

        jdbcTemplate.update(query,
                client.getClientId(),
                order.getBookingDatetime(),
                order.getPickupAddress(),
                order.getDestinationAddress(),
                order.getJourneyDistance(),
                order.getPassengerName(),
                order.getPassengerPhoneNumber(),
                order.getBookingNotes(),
                order.getPaymentType().getValue(),
                order.getCarClass().getValue(),
                order.getVehicleType().getValue(),
                order.isPayWithBonuses(),
                order.getPrice()
        );
    }

    public List<Order> getAllOrdersByClientEmail(String email) {
        Optional<CustomUser> currentUser = userDAO.findUserByEmail(email);
        List<Order> orders = new ArrayList<>();

        if (currentUser.isPresent()) {
            long clientId = currentUser.get().getUserId();

            orders = jdbcTemplate.query("select * from \"Order\" where client_id = ?",
                    new Object[]{clientId},
                    new BeanPropertyRowMapper<>(Order.class));
        }

        return orders;
    }

}
