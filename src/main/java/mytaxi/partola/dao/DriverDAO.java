package mytaxi.partola.dao;

import mytaxi.partola.models.Car;
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
            return jdbcTemplate.query("select * from \"User\" u inner join \"Driver\" d on u.user_id = d.driver_id where d.driver_id=?",
                            new Object[]{customUser.getUserId()},
                            new BeanPropertyRowMapper<>(Driver.class))
                    .stream().findAny();
        } else {
            throw new IllegalArgumentException("User " + customUser.getEmail() + " is not a Driver.");
        }
    }

    public Optional<Driver> findDriverById(long id) {
        return jdbcTemplate.query("select * from \"User\" u inner join \"Driver\" d on u.user_id = d.driver_id where d.driver_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Driver.class))
                .stream().findAny();
    }

    public void setBusyStatusById(long id, boolean status) {
        jdbcTemplate.update(
                "update \"Driver\" set busy = ? where driver_id = ?",
                status,
                id);
    }


    public List<Driver> getAllDrivers() {
        return jdbcTemplate.query("select * from \"Driver\"",
                new BeanPropertyRowMapper<>(Driver.class));
    }

    public void updateDriver(Driver driver) {

        jdbcTemplate.update(
                "UPDATE \"Driver\" SET license_number = ?, driver_rating = ?, phone_number = ?, busy = ? WHERE driver_id = ?",
                driver.getLicenseNumber(),
                driver.getDriverRating(),
                driver.getPhoneNumber(),
                driver.isBusy(),
                driver.getDriverId()
        );
    }

    public void createDriver(Driver driver) {
        jdbcTemplate.update(
                "INSERT INTO \"Driver\" (driver_id, license_number, driver_rating, phone_number, busy, car_id) VALUES (?, ?, ?, ?, ?, ?)",
                driver.getDriverId(),
                driver.getLicenseNumber(),
                driver.getDriverRating(),
                driver.getPhoneNumber(),
                driver.isBusy(),
                driver.getCarId());
    }

}
