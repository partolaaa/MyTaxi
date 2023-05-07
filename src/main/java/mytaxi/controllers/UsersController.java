package mytaxi.controllers;

import mytaxi.krutyporokh.models.Order;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Ivan Partola
 * @date 27.04.2023
 */
@Controller
public class UsersController {

    private final UserDAO userDAO;
    @Value("${googleMapsAPIKey}")
    private String googleMapsAPIKey;

    @Autowired
    public UsersController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("order")
    public String order (@ModelAttribute("order") Order order,
                         Model model) {
        model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);
        // getUsername() method returns email in our case
        String currentUserEmail = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        CustomUser currentUser = userDAO.findUserByEmail(currentUserEmail).get();
        model.addAttribute("username", currentUser.getName());
        return "order";
    }

    @GetMapping("/info")
    public String info () {
        return "/info";
    }
}
