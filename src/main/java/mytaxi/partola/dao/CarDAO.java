package mytaxi.partola.dao;

import mytaxi.partola.models.Car;
import mytaxi.partola.models.Driver;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 14.05.2023
 */
@Component
public class CarDAO {
    private final JdbcTemplate jdbcTemplate;

    public CarDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Car> getCarByDriver(Driver driver) {
        return jdbcTemplate.query("select * from \"Car\" where car_id=?",
                        new Object[]{driver.getCarId()},
                        new BeanPropertyRowMapper<>(Car.class))
                .stream().findAny();
    }
}
