package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.models.UserDriverCarForm;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CarService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.stereotype.Service;

@Service
public class UserDriverCarFormService {
    private final CustomUserService customUserService;
    private final DriverService driverService;

    private final CarService carService;

    public UserDriverCarFormService(CustomUserService customUserService, DriverService driverService, CarService carService) {
        this.customUserService = customUserService;
        this.driverService = driverService;
        this.carService = carService;
    }

    public UserDriverCarForm fillForm(long id) {
        // Get driver, car, user
        UserDriverCarForm form = new UserDriverCarForm();
        Driver driver = driverService.findDriverById(id);
        form.setCustomUser(customUserService.findUserById(id));
        form.setDriver(driver);
        form.setCar(carService.getCarByDriver(driver));

        return form;
    }

}
