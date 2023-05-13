package mytaxi.partola.controllers;

import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
@Controller
@RequestMapping("/driver")
public class DriversController {

    private final DriverDAO driverDAO;
    private final CustomUserService customUserService;

    @Autowired
    public DriversController(DriverDAO driverDAO, CustomUserService customUserService) {
        this.driverDAO = driverDAO;
        this.customUserService = customUserService;
    }

    @GetMapping("/orders")
    public String driverPage (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);

        /*Driver driver = driverDAO.getDriverByUser(customUser).get();
        Car car = driverDAO.getDriverCarByDriver(driver).get();

        System.out.println(driver);
        System.out.println(car);*/

        return "driverOrders";
    }
}
