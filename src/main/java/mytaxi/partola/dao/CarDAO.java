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

    public void updateCar(Car car) {
        jdbcTemplate.update(
                "UPDATE \"Car\" SET license_plate = ?, model = ?, color = ?, car_class = ::car_class, vehicle_type = ::vehicle_type WHERE car_id = ?",
                car.getLicensePlate(),
                car.getModel(),
                car.getColor(),
                car.getCarClass().name(),
                car.getVehicleType().name(),
                car.getCarId()
        );
    }

    public void createCar(Car car) {
        jdbcTemplate.update(
                "INSERT INTO \"Car\" (license_plate, model, color, car_class, vehicle_type) VALUES (?, ?, ?, ?::car_class, ?::vehicle_type)",
                car.getLicensePlate(),
                car.getModel(),
                car.getColor(),
                car.getCarClass().toString(),
                car.getVehicleType().toString()
        );
    }
}
