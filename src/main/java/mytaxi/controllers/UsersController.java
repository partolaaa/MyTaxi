package mytaxi.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {
    @Value("${googleMapsAPIKey}")
    private String googleMapsAPIKey;

    @GetMapping({"/", "/welcome"})
    public String welcome () {
        return "welcome";
    }

    @GetMapping("register")
    public String register () {
        return "register";
    }

    @GetMapping("order")
    public String order (Model model) {
        model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);
        return "order";
    }
}
