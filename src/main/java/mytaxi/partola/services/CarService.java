package mytaxi.partola.services;

import mytaxi.partola.dao.CarDAO;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 23.05.2023
 */
@Service
public class CarService {
    private final CarDAO carDAO;

    @Autowired
    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }


    public Car getCarByDriver(Driver driver) {
        return carDAO.getCarByDriver(driver).get();
    }
}
