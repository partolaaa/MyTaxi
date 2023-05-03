package mytaxi.controllers;

import mytaxi.partola.dao.CustomUserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {

    private final CustomUserDAO customUserDAO;
    @Value("${googleMapsAPIKey}")
    private String googleMapsAPIKey;

    @Autowired
    public UsersController(CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    @GetMapping("order")
    public String order(Model model) {
        model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);
        // getUsername() method returns email in our case
        String currentUserEmail = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        CustomUser currentUser = customUserDAO.findUserByEmail(currentUserEmail).get();
        model.addAttribute("username", currentUser.getName());
        return "order";
    }

    @GetMapping("/info")
    public String info () {
        return "/info";
    }
}
