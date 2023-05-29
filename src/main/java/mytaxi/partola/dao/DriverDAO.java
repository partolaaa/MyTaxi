package mytaxi.partola.dao;

import mytaxi.partola.models.Car;
import mytaxi.krutyporokh.models.Order;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
@Component
public class DriverDAO {
    private final JdbcTemplate jdbcTemplate;
    private final CarDAO carDAO;

    public DriverDAO(JdbcTemplate jdbcTemplate, CarDAO carDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.carDAO = carDAO;
    }


    public Optional<Driver> findDriverByUser(CustomUser customUser) {
        if (customUser.getRole().equals(Role.ROLE_DRIVER)) {
            return jdbcTemplate.query("select * from \"user\" u inner join \"driver\" d on u.user_id = d.driver_id where d.driver_id=?",
                            new Object[]{customUser.getUserId()},
                            new BeanPropertyRowMapper<>(Driver.class))
                    .stream().findAny();
        } else {
            throw new IllegalArgumentException("User " + customUser.getEmail() + " is not a Driver.");
        }
    }

    public Optional<Driver> findDriverById(long id) {
        return jdbcTemplate.query("select * from \"user\" u inner join \"driver\" d on u.user_id = d.driver_id where d.driver_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Driver.class))
                .stream().findAny();
    }

    public void setBusyStatusById(long id, boolean status) {
        jdbcTemplate.update(
                "update \"driver\" set busy = ? where driver_id = ?",
                status,
                id);
    }


    public List<Driver> getAllDrivers() {
        return jdbcTemplate.query("select * from \"user\" u inner join \"driver\" d on u.user_id = d.driver_id",
                new BeanPropertyRowMapper<>(Driver.class));
    }

    public void updateDriver(Driver driver) {
        jdbcTemplate.update(
                "UPDATE \"driver\" SET license_number = ?, rating = ?, phone_number = ?, busy = ?, car_id = ?, number_of_ratings = ?, total_ratings = ? WHERE driver_id = ?",
                driver.getLicenseNumber(),
                driver.getRating(),
                driver.getPhoneNumber(),
                driver.isBusy(),
                driver.getCarId(),
                driver.getNumberOfRatings(),
                driver.getTotalRatings(),
                driver.getDriverId()
        );
    }

    public void createDriver(Driver driver) {

        long userId = jdbcTemplate.queryForObject("SELECT currval('user_id_seq')", Long.class);
        long carId = jdbcTemplate.queryForObject("SELECT currval('car_id_seq')", Long.class);

        jdbcTemplate.update(
                "INSERT INTO \"driver\" (driver_id, license_number, phone_number, car_id) VALUES (?, ?, ?, ?)",
                userId,
                driver.getLicenseNumber(),
                driver.getPhoneNumber(),
                carId
        );

        jdbcTemplate.update("UPDATE \"user\" SET role='ROLE_DRIVER' WHERE user_id=?", userId);
    }

    public void updateRating (Driver driver) {
        jdbcTemplate.update(
                "update \"driver\" " +
                        "set rating = ?," +
                        "number_of_ratings = ?," +
                        "total_ratings = ?" +
                        " where driver_id = ?",
                driver.getRating(),
                driver.getNumberOfRatings(),
                driver.getTotalRatings(),
                driver.getDriverId());
    }
}
