package mytaxi.partola.services;

import mytaxi.krutyporokh.models.UserDriverCarForm;
import mytaxi.partola.dao.CarDAO;
import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private final DriverDAO driverDAO;
    private final CustomUserService customUserService;
    private final CarDAO carDAO;

    public DriverService(DriverDAO driverDAO, CustomUserService customUserService, CarDAO carDAO) {
        this.driverDAO = driverDAO;
        this.customUserService = customUserService;
        this.carDAO = carDAO;
    }

    public void updateDriver(UserDriverCarForm userDriverCarForm) {
        customUserService.updateUser(userDriverCarForm.getCustomUser());
        carDAO.updateCar(userDriverCarForm.getCar());
        driverDAO.updateDriver(userDriverCarForm.getDriver());
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public void createDriver(UserDriverCarForm userDriverCarForm) {
        customUserService.createUser(userDriverCarForm.getCustomUser());
        carDAO.createCar(userDriverCarForm.getCar());
        driverDAO.createDriver(userDriverCarForm.getDriver());
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
