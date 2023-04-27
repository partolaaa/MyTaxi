package mytaxi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {

    @GetMapping("/")
    public String welcome () {
        return "welcome";
    }

    @GetMapping("register")
    public String register () {
        return "register";
    }

    @GetMapping("order")
    public String order () {
        return "order";
    }
}
