package mytaxi.partola.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {
    @GetMapping("/info")
    public String info () {
        return "user/info";
    }

}
