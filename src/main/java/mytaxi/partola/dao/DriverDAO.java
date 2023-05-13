package mytaxi.partola.dao;

import mytaxi.partola.models.Car;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.models.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
@Component
public class DriverDAO {
    private final UserDAO userDAO;
    private final JdbcTemplate jdbcTemplate;

    public DriverDAO(UserDAO userDAO, JdbcTemplate jdbcTemplate) {
        this.userDAO = userDAO;
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Driver> getDriverByUser (CustomUser customUser) {
        if (customUser.getRole().equals(Role.ROLE_DRIVER)) {
            return jdbcTemplate.query("select * from \"Driver\" where driver_id=?",
                            new Object[]{customUser.getUserId()},
                            new BeanPropertyRowMapper<>(Driver.class))
                    .stream().findAny();
        } else {
            throw new IllegalArgumentException("User " + customUser.getEmail() + " is not a Driver.");
        }
    }

    public Optional<Car> getDriverCarByDriver (Driver driver) {
        return jdbcTemplate.query("select * from \"Car\" where car_id=?",
                        new Object[]{driver.getCarId()},
                        new BeanPropertyRowMapper<>(Car.class))
                .stream().findAny();
    }
}
