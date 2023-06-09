package mytaxi.partola.dao;

import mytaxi.partola.models.Order;
import mytaxi.partola.models.OrderStatus;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CarDAO carDAO;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate, CarDAO carDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.carDAO = carDAO;
    }

    public void createNewOrder(Order order, Client client) {
        String query = "INSERT INTO \"order\" " +
                "(client_id, booking_datetime, pickup_address, destination_address, journey_distance, passenger_name, " +
                "passenger_phone_number, booking_notes, payment_type, car_class, vehicle_type, pay_with_bonuses, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::payment_type, ?::car_class, ?::vehicle_type, ?, ?)";

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
        return jdbcTemplate.query("select * from \"order\" where client_id = ?",
                new Object[]{clientId},
                new BeanPropertyRowMapper<>(Order.class));
    }

    // For correct calculating bonus percent, because cancelled orders were not finished
    public List<Order> findFinishedOrdersByClientId(long clientId) {
        return jdbcTemplate.query("select * from \"order\" where client_id = ? and order_status != 'CANCELLED'::order_status",
                new Object[]{clientId},
                new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> findAllFinishedOrdersByDriverId(long id) {
        return jdbcTemplate.query("SELECT * FROM \"order\" WHERE driver_id = ? AND (order_status = 'COMPLETED'::order_status " +
                        "OR order_status = 'RATED_BY_CLIENT'::order_status " +
                        "OR order_status = 'RATED_BY_DRIVER'::order_status " +
                        "OR order_status = 'RATED_BY_ALL'::order_status)",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Order.class));
    }

    public Optional<Order> findActiveOrderByDriverId(long id) {
        return jdbcTemplate.query("select * from \"order\" where order_status in ('ACCEPTED'::order_status, " +
                                "'WAITING_FOR_CLIENT'::order_status, " +
                                "'IN_PROCESS'::order_status) " +
                                "and driver_id = ?;",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public List<Order> findAllOrdersForDriver(Driver driver) {
        Car car = carDAO.getCarByDriver(driver).get();

        return jdbcTemplate.query("select  * from \"order\"\n" +
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
        return jdbcTemplate.query("select * from \"order\" where order_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public Optional<Order> findOrderByHash(String hash) {
        return jdbcTemplate.query("select * from \"order\" where hash=?",
                        new Object[]{hash},
                        new BeanPropertyRowMapper<>(Order.class))
                .stream().findAny();
    }

    public void setOrderStatus(String hash, OrderStatus orderStatus) {
        jdbcTemplate.update("update \"order\" set order_status = ?::order_status where hash = ?",
                orderStatus.getValue(),
                hash);
    }

    public void assignDriver(Driver driver, Order order) {
        jdbcTemplate.update("update \"order\" set driver_id = ? where order_id = ?",
                driver.getDriverId(),
                order.getOrderId());
    }
}
