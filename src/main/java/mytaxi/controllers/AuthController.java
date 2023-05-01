package mytaxi.controllers;

import mytaxi.partola.dao.CustomUserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.util.CustomUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Controller
public class AuthController {
    private final CustomUserDAO customUserDAO;
    private final CustomUserValidator customUserValidator;

    @Autowired
    public AuthController(CustomUserDAO customUserDAO, CustomUserValidator customUserValidator) {
        this.customUserDAO = customUserDAO;
        this.customUserValidator = customUserValidator;
    }

    @GetMapping("register")
    public String register (@ModelAttribute("customUser") CustomUser customUser) {
        return "register";
    }

    @PostMapping("register")
    public String registerNewUser(@ModelAttribute("customUser") @Valid CustomUser customUser,
                                  BindingResult bindingResult,
                                  Model model) {

        customUserValidator.validate(customUser, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "register";
        }
        customUserDAO.createUser(customUser);
        return "redirect:/login";
    }

    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }
}
