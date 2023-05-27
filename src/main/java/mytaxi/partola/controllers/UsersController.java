package mytaxi.partola.controllers;
import mytaxi.partola.services.CustomUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {
    private final CustomUserService customUserService;

    public UsersController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @GetMapping("/info")
    public String info (Model model) {
        boolean isUserLoggedIn = customUserService.isLoggedIn();
        model.addAttribute("isLogged", isUserLoggedIn);

        if (isUserLoggedIn) {
            model.addAttribute("user", customUserService.getCurrentUserFromSession().get());
        }
        return "user/info";
    }

}
