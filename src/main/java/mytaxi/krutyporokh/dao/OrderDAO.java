package mytaxi.krutyporokh.dao;

import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.partola.dao.CarDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;
    private final CarDAO carDAO;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate, UserDAO userDAO, CarDAO carDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
        this.carDAO = carDAO;
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

    public List<Order> findAllOrdersByClientId(long clientId) {
        return jdbcTemplate.query("select * from \"Order\" where client_id = ?",
                new Object[]{clientId},
                new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> findAllFinishedOrdersByDriverId(long id) {
        return jdbcTemplate.query("SELECT * FROM \"Order\" WHERE driver_id = ? AND (order_status = 'COMPLETED'::order_status " +
                        "OR order_status = 'RATED_BY_CLIENT'::order_status " +
                        "OR order_status = 'RATED_BY_DRIVER'::order_status " +
                        "OR order_status = 'RATED_BY_ALL'::order_status)",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Order.class));
    }

    public Optional<Order> findActiveOrderByDriverId(long id) {
        return jdbcTemplate.query("select * from \"Order\" where order_status in ('ACCEPTED'::order_status, " +
                                "'WAITING_FOR_CLIENT'::order_status, " +
                                "'IN_PROCESS'::order_status) " +
                                "and driver_id = ?;",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public List<Order> findAllOrdersForDriver(Driver driver) {
        Car car = carDAO.getCarByDriver(driver).get();

        return jdbcTemplate.query("select  * from \"Order\"\n" +
                        "where\n" +
                        "    vehicle_type = ?::vehicle_type\n" +
                        "    and\n" +
                        "    car_class = ?::car_class\n" +
                        "    and\n" +
                        "    order_status = 'NOT_ACCEPTED'::order_status;",
                new Object[]{
                        car.getVehicleType().getValue(),
                        car.getCarClass().getValue()},
                new BeanPropertyRowMapper<>(Order.class));
    }

    public Optional<Order> findOrderById(long id) {
        return jdbcTemplate.query("select * from \"Order\" where order_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public void setOrderStatus(Order order, OrderStatus orderStatus) {
        jdbcTemplate.update("update \"Order\" set order_status = ?::order_status where order_id = ?",
                orderStatus.getValue(),
                order.getOrderId());
    }

    public void assignDriver(Driver driver, Order order) {
        jdbcTemplate.update("update \"Order\" set driver_id = ? where order_id = ?",
                driver.getDriverId(),
                order.getOrderId());
    }
}
