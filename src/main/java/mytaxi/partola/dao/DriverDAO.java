package mytaxi.partola.dao;

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
    private final JdbcTemplate jdbcTemplate;

    public DriverDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
