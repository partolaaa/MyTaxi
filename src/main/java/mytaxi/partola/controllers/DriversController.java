package mytaxi.partola.controllers;

import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
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

    private final UserDAO userDAO;
    private final CustomUserService customUserService;

    @Autowired
    public DriversController(UserDAO userDAO, CustomUserService customUserService) {
        this.userDAO = userDAO;
        this.customUserService = customUserService;
    }

    @GetMapping("/orders")
    public String driverPage (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);
        return "driverOrders";
    }
}
