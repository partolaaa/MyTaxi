package mytaxi.partola.services;

import mytaxi.partola.dao.CarDAO;
import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private final DriverDAO driverDAO;
    private final UserDAO userDAO;
    private final CarDAO carDAO;

    public DriverService(DriverDAO driverDAO, UserDAO userDAO, CarDAO carDAO) {
        this.driverDAO = driverDAO;
        this.userDAO = userDAO;
        this.carDAO = carDAO;
    }

    public void updateDriver(Driver driver, CustomUser user, Car car) {
        userDAO.updateUser(user);
        driverDAO.updateDriver(driver);
        carDAO.updateCar(car);

    }

    public List<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public void createDriver(CustomUser user, Driver driver, Car car) {
        userDAO.createUser(user);
        driverDAO.createDriver(driver);
        carDAO.createCar(car);
    }
    public void updateRating(Driver driver, int rating) {
        driver.setNumberOfRatings(driver.getNumberOfRatings() + 1);
        driver.setTotalRatings(driver.getTotalRatings() + rating);
        driver.setRating((float)driver.getTotalRatings() / driver.getNumberOfRatings());

        driverDAO.updateRating(driver);
    }

    public Driver findDriverById(long id) {
        return driverDAO.findDriverById(id).get();
    }

    public Driver findDriverByUser(CustomUser customUser) {
        return driverDAO.findDriverByUser(customUser).get();
    }

    public void setBusyStatusById(long id, boolean status) {
        driverDAO.setBusyStatusById(id, status);
    }
}
