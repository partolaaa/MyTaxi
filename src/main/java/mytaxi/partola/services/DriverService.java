package mytaxi.partola.services;

import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 18.05.2023
 */
@Service
public class DriverService {
    private final DriverDAO driverDAO;

    public DriverService(DriverDAO driverDAO) {
        this.driverDAO = driverDAO;
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
